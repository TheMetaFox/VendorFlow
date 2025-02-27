package com.example.vendorflow.ui.screens.catalog

import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.SortType
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.entities.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(private val vendorRepository: VendorRepository): ViewModel() {

    private val _sortType: MutableStateFlow<SortType> = MutableStateFlow(value = SortType.NAME)

    private val _catalogList: StateFlow<List<Product>> = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.NAME -> vendorRepository.getProductsOrderedByName()
                SortType.BARCODE -> vendorRepository.getProductsOrderedByBarcode()
                SortType.PRICE -> vendorRepository.getProductsOrderedByPrice()
                SortType.COST -> vendorRepository.getProductsOrderedByCost()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<CatalogState> = MutableStateFlow(value = CatalogState())
    val state: StateFlow<CatalogState> = combine(
        flow = _state,
        flow2 = _sortType,
        flow3 = _catalogList,
        transform = { state, sortType, catalogList ->
            state.copy(
                catalogList = catalogList,
                sortType = sortType
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
                        isShowingCatalogItemDialog = false
                    )
                }
            }
            is CatalogEvent.ShowCatalogItemDialog -> {
                _state.update {
                    it.copy(
                        isShowingCatalogItemDialog = true
                    )
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
                    "Product ID" -> {
                        _state.update { catalogState ->
                            catalogState.copy(productIdField = catalogEvent.text)
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
                val productName: String = _state.value.productNameField
                val collectionName: String = _state.value.collectionField
                val imageUri: Uri? = _state.value.productImageUri
                val productId: String = _state.value.productIdField
                val price: String = _state.value.priceField
                val cost: String = _state.value.costField

                if (imageUri == null) {
                    Log.i("CatalogViewModel.kt","Image field is empty...")
                    return
                }
                catalogEvent.context.contentResolver.takePersistableUriPermission(
                    imageUri,
                    FLAG_GRANT_READ_URI_PERMISSION
                )

                if (productName.isBlank()) {
                    Log.i("CatalogViewModel.kt","Name field is blank...")
                    return
                }
                if (collectionName.isBlank()) {
                    Log.i("CatalogViewModel.kt","Collection field is blank...")
                    return
                }
                if (productId.isBlank()) {
                    Log.i("CatalogViewModel.kt", "ID field is blank...")
                    return
                }
                try {
                    productId.toInt()
                } catch (e: NumberFormatException) {
                    Log.i("CatalogViewModel.kt","ID field must be a number between 100000-999999...")
                    return
                }
                if (price.isBlank()) {
                    Log.i("CatalogViewModel.kt","Price field is blank...")
                    return
                }
                try {
                    price.toFloat()
                } catch (e: NumberFormatException) {
                    Log.i("CatalogViewModel.kt","Price must be a number...")
                    return
                }
                if (cost.isBlank()) {
                    Log.i("CatalogViewModel.kt","Cost field is blank...")
                    return
                }
                try {
                    cost.toFloat()
                } catch (e: NumberFormatException) {
                    Log.i("CatalogViewModel.kt","Cost must be a number...")
                    return
                }

                val product = Product(
                    productId = productId.toInt(),
                    productName = productName,
                    collectionName = collectionName,
                    image = imageUri,
                    price = price.toFloat(),
                    cost = cost.toFloat(),
                    stock = 0,
                )

                Log.i("CatalogViewModel.kt","Upserting $product to database...")
                viewModelScope.launch {
                    vendorRepository.upsertProduct(
                        product = product
                    )
                }
                _state.update {
                    it.copy(
                        productImageUri = null,
                        productNameField = "",
                        collectionField = "",
                        productIdField = "",
                        priceField = "",
                        costField = "",
                    )
                }
            }
        }
    }
}