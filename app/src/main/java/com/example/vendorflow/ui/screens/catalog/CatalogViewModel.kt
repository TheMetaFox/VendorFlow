package com.example.vendorflow.ui.screens.catalog

import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.logic.DeleteProductUseCase
import com.example.vendorflow.logic.GetCollectionUseCase
import com.example.vendorflow.logic.GetProductUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.SyncAppToNotionUseCase
import com.example.vendorflow.logic.SyncNotionToAppUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val upsertProductUseCase: UpsertProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val syncAppToNotionUseCase: SyncAppToNotionUseCase,
    private val syncNotionToAppUseCase: SyncNotionToAppUseCase,
    getProductsOrderedByNameUseCase: GetProductsOrderedByNameUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val getCollectionUseCase: GetCollectionUseCase
): ViewModel() {

    private val _catalogList: StateFlow<List<Product>> = getProductsOrderedByNameUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<CatalogState> = MutableStateFlow(value = CatalogState())
    val state: StateFlow<CatalogState> = combine(
        flow = _state,
        flow2 = _catalogList,
        transform = { state, catalogList ->
            state.copy(
                catalogList = catalogList
            )
        }
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CatalogState()
        )

    fun onEvent(catalogEvent: CatalogEvent) {
        when (catalogEvent) {
            is CatalogEvent.HideCatalogItemDialog -> {
                _state.update {
                    it.copy(
                        isShowingCatalogItemDialog = false,
                        catalogItemId = null
                    )
                }
            }
            is CatalogEvent.ShowCatalogItemDialog -> {
                viewModelScope.launch {
                    if (catalogEvent.item != null) {
                        _state.update { catalogState ->
                            catalogState.copy(
                                catalogItemId = catalogEvent.item.productId,
                                productImageUri = catalogEvent.item.image,
                                productNameField = catalogEvent.item.productName,
                                collectionField = if (getCollectionUseCase(collectionId = catalogEvent.item.collectionId) == null) "Miscellaneous" else getCollectionUseCase(collectionId = catalogEvent.item.collectionId)!!.collectionName,
                                priceField = catalogEvent.item.price.toString(),
                                costField = catalogEvent.item.cost.toString()
                            )
                        }
                    } else {
                        _state.update { catalogState ->
                            catalogState.copy(
                                catalogItemId = null,
                                productImageUri = Uri.EMPTY,
                                productNameField = "",
                                collectionField = "",
                                priceField = "",
                                costField = ""
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isShowingCatalogItemDialog = true
                        )
                    }
                }
            }
            is CatalogEvent.UpdateVendorFlow -> {
                _state.update {
                    it.copy(
                        syncSource = SyncSource.NOTION
                    )
                }
                viewModelScope.launch {
                    syncAppToNotionUseCase()
                }
            }
            is CatalogEvent.UpdateNotion -> {
                _state.update {
                    it.copy(
                        syncSource = SyncSource.VENDOR_FLOW
                    )
                }
                viewModelScope.launch {
                    syncNotionToAppUseCase()
                }
            }
            is CatalogEvent.UpdateTextField -> {
                when (catalogEvent.textField) {
                    "Name" -> {
                        _state.update { catalogState ->
                            catalogState.copy(productNameField = catalogEvent.text)
                        }
                    }
                    "Collection" -> {
                        _state.update { catalogState ->
                            catalogState.copy(collectionField = catalogEvent.text)
                        }
                    }
                    "Price" -> {
                        _state.update { catalogState ->
                            catalogState.copy(priceField = catalogEvent.text)
                        }
                    }
                    "Cost" -> {
                        _state.update { catalogState ->
                            catalogState.copy(costField = catalogEvent.text)
                        }
                    }
                }
            }
            is CatalogEvent.UpdateImageField -> {
                _state.update { catalogState ->
                    Log.i("CatalogViewModel.kt", "Uri: ${catalogEvent.imageUri}")

                    catalogState.copy(productImageUri = catalogEvent.imageUri)
                }
            }
            is CatalogEvent.UpsertCatalogItem -> {
                viewModelScope.launch {
                    val productName: String = _state.value.productNameField.ifBlank { "..." }.trim()
                    val collectionName: String = _state.value.collectionField.ifBlank { "Miscellaneous" }.trim()
                    val imageUri: Uri = if (_state.value.productImageUri == Uri.EMPTY) Uri.EMPTY else _state.value.productImageUri
                    var price: String = _state.value.priceField.ifBlank { "0" }
                    var cost: String = _state.value.costField.ifBlank { "0" }

                    if (imageUri != Uri.EMPTY) {
                        try {
                            catalogEvent.context.contentResolver.takePersistableUriPermission(
                                imageUri,
                                FLAG_GRANT_READ_URI_PERMISSION
                            )
                        } catch (e: SecurityException) {
                            Log.i("CatalogViewModel.kt", "Permission for $productName image not found...")
                        }
                    }

                    var collectionId: Int? = getCollectionUseCase(collectionName = collectionName)?.collectionId
                    if (collectionId == null) {
                        Log.i("CatalogViewModel.kt","Collection does not exist...")
                        collectionId = getCollectionUseCase(collectionName = "Miscellaneous")?.collectionId
                    }

                    try {
                        price.toFloat()
                    } catch (e: NumberFormatException) {
                        Log.i("CatalogViewModel.kt","Setting price to $0.00...")
                        price = "0"
                    }
                    try {
                        cost.toFloat()
                    } catch (e: NumberFormatException) {
                        Log.i("CatalogViewModel.kt","Setting cost to $0.00...")
                        cost = "0"
                    }

                    val product = Product(
                        productId = if (_state.value.catalogItemId == null) 0 else _state.value.catalogItemId!!,
                        productName = productName,
                        collectionId = collectionId!!,
                        image = imageUri,
                        price = price.toFloat(),
                        cost = cost.toFloat(),
                        stock = if (_state.value.catalogItemId == null) 0 else getProductUseCase(productId = _state.value.catalogItemId!!).stock,
                    )

                    Log.i("CatalogViewModel.kt","Upserting $product to database...")
                    upsertProductUseCase(product = product)

                    _state.update {
                        it.copy(
                            catalogItemId = null,
                            productImageUri = Uri.EMPTY,
                            productNameField = "",
                            collectionField = "",
                            priceField = "",
                            costField = "",
                        )
                    }
                }
            }
            is CatalogEvent.DeleteCatalogItem -> {
                viewModelScope.launch {
                    deleteProductUseCase(product = catalogEvent.item)
                }
            }

            CatalogEvent.HideConfirmationDialog -> {
                _state.update {
                    it.copy(isShowingConfirmationDialog = false
                    )
                }
            }
            is CatalogEvent.ShowConfirmationDialog -> {
                _state.update {
                    it.copy(
                        isShowingConfirmationDialog = true,
                        syncSource = catalogEvent.source
                    )
                }
            }
        }
    }
}