package com.example.vendorflow.ui.screens.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.logic.GetInventoryCostUseCase
import com.example.vendorflow.logic.GetInventoryPriceUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
//    private val vendorRepository: VendorRepository,
    private val upsertProductUseCase: UpsertProductUseCase,
    getProductsOrderedByNameUseCase: GetProductsOrderedByNameUseCase,
    getInventoryPriceUseCase: GetInventoryPriceUseCase,
    getInventoryCostUseCase: GetInventoryCostUseCase
): ViewModel() {

//    private val _inventoryList: StateFlow<List<Product>> = vendorRepository.getProductsOrderedByName()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = emptyList()
//        )
//    private val _totalInventoryPrice: StateFlow<Float> = vendorRepository.getTotalInventoryPrice()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = 0f
//        )
//    private val _totalInventoryCost: StateFlow<Float> = vendorRepository.getTotalInventoryCost()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = 0f
//        )
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


    private val _state: MutableStateFlow<InventoryState> = MutableStateFlow(value = InventoryState())
    val state: StateFlow<InventoryState> = combine(
        flow = _state,
        flow2 = _inventoryList,
        flow3 = _totalInventoryPrice,
        flow4 = _totalInventoryCost,
        transform = { state, inventoryList, totalInventoryPrice, totalInventoryCost ->
            state.copy(
                inventoryList = inventoryList,
                totalInventoryPrice = totalInventoryPrice,
                totalInventoryCost = totalInventoryCost
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
//                    vendorRepository.increaseProductStock(product = inventoryEvent.product)
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
//                    vendorRepository.decreaseProductStock(product = inventoryEvent.product)
                    upsertProductUseCase(
                        product = inventoryEvent.product.copy(
                            stock = inventoryEvent.product.stock - 1
                        )
                    )
                    upsertProductUseCase
                }
            }
        }
    }
}