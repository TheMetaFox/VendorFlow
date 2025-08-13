package com.example.vendorflow.ui.screens.inventory

import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.logic.DeleteProductUseCase
import com.example.vendorflow.logic.GetInventoryCostUseCase
import com.example.vendorflow.logic.GetInventoryPriceUseCase
import com.example.vendorflow.logic.GetProductUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.SyncAppToNotionUseCase
import com.example.vendorflow.logic.SyncNotionToAppUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import com.example.vendorflow.data.enums.SyncSource
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.logic.GetTagsFromProductIdUseCase
import com.example.vendorflow.logic.GetTagsOrderedByOrdinalUseCase
import com.example.vendorflow.logic.UpsertProductTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(
    getProductsOrderedByNameUseCase: GetProductsOrderedByNameUseCase,
    getInventoryPriceUseCase: GetInventoryPriceUseCase,
    getInventoryCostUseCase: GetInventoryCostUseCase,
    getTagsOrderedByOrdinalUseCase: GetTagsOrderedByOrdinalUseCase,
    private val upsertProductUseCase: UpsertProductUseCase,
    private val upsertProductTagsUseCase: UpsertProductTagsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val syncAppToNotionUseCase: SyncAppToNotionUseCase,
    private val syncNotionToAppUseCase: SyncNotionToAppUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getTagsFromProductIdUseCase: GetTagsFromProductIdUseCase
    ): ViewModel() {

    private val _inventoryList: StateFlow<List<Product>> = getProductsOrderedByNameUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _totalInventoryPrice: StateFlow<Float> = getInventoryPriceUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0f
        )

    private val _totalInventoryCost: StateFlow<Float> = getInventoryCostUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = 0f
        )

    private val _tagList: StateFlow<List<Tag>> = getTagsOrderedByOrdinalUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<InventoryState> = MutableStateFlow(value = InventoryState())
    val state: StateFlow<InventoryState> = combine(
        flow = _state,
        flow2 = _inventoryList,
        flow3 = _totalInventoryPrice,
        flow4 = _totalInventoryCost,
        flow5 = _tagList,
        transform = { state, inventoryList, totalInventoryPrice, totalInventoryCost, tagList ->
            state.copy(
                inventoryList = inventoryList.filter {
                    state.searchText.isBlank() or it.productName.contains(other = state.searchText, ignoreCase = true)
                },
                totalInventoryPrice = totalInventoryPrice,
                totalInventoryCost = totalInventoryCost,
                tagList = tagList
            )
        }
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InventoryState()
        )

        fun onEvent(inventoryEvent: InventoryEvent) {
        when (inventoryEvent) {
            is InventoryEvent.IncreaseProductStock -> {
                viewModelScope.launch {
                    upsertProductUseCase(
                        product = inventoryEvent.product.copy(
                            stock = inventoryEvent.product.stock + 1
                        )
                    )
                }
            }
            is InventoryEvent.DecreaseProductStock -> {
                viewModelScope.launch {
                    if (inventoryEvent.product.stock == 0) return@launch
                    upsertProductUseCase(
                        product = inventoryEvent.product.copy(
                            stock = inventoryEvent.product.stock - 1
                        )
                    )
                    upsertProductUseCase
                }
            }
            is InventoryEvent.UpdateSearchField -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            searchText = inventoryEvent.text
                        )
                    }
                }
            }
            InventoryEvent.HideProductDialog -> {
                _state.update {
                    it.copy(
                        isShowingProductDialog = false,
                        productItemId = null
                    )
                }
            }
            is InventoryEvent.ShowProductDialog -> {
                viewModelScope.launch {
                    if (inventoryEvent.product != null) {
                        _state.update { catalogState ->
                            catalogState.copy(
                                productItemId = inventoryEvent.product.productId,
                                productImageUri = inventoryEvent.product.image,
                                productNameField = inventoryEvent.product.productName,
                                priceField = inventoryEvent.product.price.toString(),
                                costField = inventoryEvent.product.cost.toString(),
                                selectedTags = getTagsFromProductIdUseCase(productId = inventoryEvent.product.productId)
                            )
                        }
                    } else {
                        _state.update { catalogState ->
                            catalogState.copy(
                                productItemId = null,
                                productImageUri = Uri.EMPTY,
                                productNameField = "",
                                priceField = "",
                                costField = "",
                                selectedTags = emptyList()
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isShowingProductDialog = true
                        )
                    }
                }
            }
            InventoryEvent.UpdateVendorFlow -> {
                _state.update {
                    it.copy(
                        syncSource = SyncSource.NOTION
                    )
                }
                viewModelScope.launch {
                    syncAppToNotionUseCase()
                }
            }
            InventoryEvent.UpdateNotion -> {
                _state.update {
                    it.copy(
                        syncSource = SyncSource.VENDOR_FLOW
                    )
                }
                viewModelScope.launch {
                    syncNotionToAppUseCase()
                }
            }
            is InventoryEvent.UpdateTextField -> {
                when (inventoryEvent.textField) {
                    "Name" -> {
                        _state.update { catalogState ->
                            catalogState.copy(productNameField = inventoryEvent.text)
                        }
                    }
                    "Price" -> {
                        _state.update { catalogState ->
                            catalogState.copy(priceField = inventoryEvent.text)
                        }
                    }
                    "Cost" -> {
                        _state.update { catalogState ->
                            catalogState.copy(costField = inventoryEvent.text)
                        }
                    }
                }
            }
            is InventoryEvent.UpdateImageField -> {
                _state.update {
                    Log.i("CatalogViewModel.kt", "Uri: ${inventoryEvent.imageUri}")

                    it.copy(productImageUri = inventoryEvent.imageUri)
                }
            }
            is InventoryEvent.UpdateSelectedTags -> {
                val selectedTags: List<Tag> = _state.value.selectedTags.toList()
                _state.update {
                    it.copy(
                        selectedTags = if (!selectedTags.contains(element = inventoryEvent.tag)) selectedTags.plus(element = inventoryEvent.tag) else selectedTags.minus(element = inventoryEvent.tag)
                    )
                }
            }
            is InventoryEvent.UpsertProductItem -> {
                viewModelScope.launch {
                    val productName: String = _state.value.productNameField.ifBlank { "..." }.trim()
                    val imageUri: Uri = if (_state.value.productImageUri == Uri.EMPTY) Uri.EMPTY else _state.value.productImageUri
                    var price: String = _state.value.priceField.ifBlank { "0" }
                    var cost: String = _state.value.costField.ifBlank { "0" }
                    val selectedTags: List<Tag> = _state.value.selectedTags

                    if (imageUri != Uri.EMPTY) {
                        try {
                            inventoryEvent.context.contentResolver.takePersistableUriPermission(
                                imageUri,
                                FLAG_GRANT_READ_URI_PERMISSION
                            )
                        } catch (_: SecurityException) {
                            Log.i("InventoryViewModel.kt", "Permission for $productName image not found...")
                        }
                    }

                    try {
                        price.toFloat()
                    } catch (_: NumberFormatException) {
                        Log.i("InventoryViewModel.kt","Setting price to $0.00...")
                        price = "0"
                    }
                    try {
                        cost.toFloat()
                    } catch (_: NumberFormatException) {
                        Log.i("InventoryViewModel.kt","Setting cost to $0.00...")
                        cost = "0"
                    }

                    val product = Product(
                        productId = if (_state.value.productItemId == null) 0 else _state.value.productItemId!!,
                        productName = productName,
                        image = imageUri,
                        price = price.toFloat(),
                        cost = cost.toFloat(),
                        stock = if (_state.value.productItemId == null) 0 else getProductUseCase(productId = _state.value.productItemId!!).stock,
                    )

                    Log.i("InventoryViewModel.kt","Upserting $product to database...")
                    upsertProductUseCase(product = product)
                    upsertProductTagsUseCase(product = product, selectedTags = selectedTags)
                    _state.update {
                        it.copy(
                            productItemId = null,
                            productImageUri = Uri.EMPTY,
                            productNameField = "",
                            priceField = "",
                            costField = "",
                            selectedTags = emptyList()
                        )
                    }
                }
            }
            is InventoryEvent.DeleteProductItem -> {
                viewModelScope.launch {
                    deleteProductUseCase(product = inventoryEvent.product)
                }
            }
            InventoryEvent.HideConfirmationDialog -> {
                _state.update {
                    it.copy(isShowingConfirmationDialog = false
                    )
                }
            }
            is InventoryEvent.ShowConfirmationDialog -> {
                _state.update {
                    it.copy(
                        isShowingConfirmationDialog = true,
                        syncSource = inventoryEvent.source
                    )
                }
            }
        }
    }
}