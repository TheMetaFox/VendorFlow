package com.example.vendorflow.ui.screens.transation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.PaymentMethod
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.entities.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class TransactionViewModel(private val vendorRepository: VendorRepository): ViewModel() {

    private val _productList: StateFlow<List<Product>> = vendorRepository.getProductsOrderedByPrice()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<TransactionState> = MutableStateFlow(value = TransactionState())
    open val state: StateFlow<TransactionState> = combine(
        flow = _state,
        flow2 = _productList,
        transform = { state, productList ->
            state.copy(
                productList = productList,
            )
        }
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionState()
        )

    fun onEvent(transactionEvent: TransactionEvent) {
        when (transactionEvent) {
            is TransactionEvent.IncreaseItemQuantity -> {
                val itemQuantityList: MutableMap<Product, Int> = _state.value.itemQuantityList.toMutableMap()

                if (itemQuantityList[transactionEvent.product] == transactionEvent.product.stock) {
                    Log.i("TransactionViewModel.kt", "${transactionEvent.product.productName} is out of stock...")
                    return
                } else {
                    Log.i("TransactionViewModel.kt", "Increasing ${transactionEvent.product.productName} quantity...")
                    itemQuantityList[transactionEvent.product] = itemQuantityList.getOrDefault(transactionEvent.product, 0)+1
                }

                _state.update {
                    it.copy(
                        itemQuantityList = itemQuantityList
                    )
                }
            }
            is TransactionEvent.DecreaseItemQuantity -> {
                val itemQuantityList: MutableMap<Product, Int> = _state.value.itemQuantityList.toMutableMap()

                if (!itemQuantityList.contains(transactionEvent.product)) {
                    Log.i("TransactionViewModel.kt", "Negative quantities are not allowed...")
                    return
                }

                if (itemQuantityList[transactionEvent.product] == 1) {
                    Log.i("TransactionViewModel.kt", "${transactionEvent.product.productName} removed...")
                    itemQuantityList.remove(transactionEvent.product)
                } else {
                    Log.i("TransactionViewModel.kt", "Decreasing ${transactionEvent.product.productName} quantity...")
                    itemQuantityList[transactionEvent.product] = itemQuantityList.getOrDefault(transactionEvent.product, 0)-1
                }
                _state.update {
                    it.copy(
                        itemQuantityList = itemQuantityList
                    )
                }
            }
            is TransactionEvent.DisplayItemizedTransaction -> {
                _state.update {
                    it.copy(
                        itemQuantityList = transactionEvent.itemList
                    )
                }
            }
            is TransactionEvent.CalculateTotal -> {
                val itemQuantityList: Map<Product, Int> = _state.value.itemQuantityList
                var total = 0f

                itemQuantityList.forEach { (product, quantity) ->
                    total += product.price*quantity
                }

                _state.update {
                    it.copy(
                        totalAmount = total
                    )
                }
            }
            is TransactionEvent.SetPaymentMethod -> {
                _state.update {
                    it.copy(
                        paymentMethod = transactionEvent.paymentMethod
                    )
                }
            }
            is TransactionEvent.ProcessTransaction -> {
                val itemQuantityList: Map<Product, Int> = _state.value.itemQuantityList
                val totalAmount: Float = _state.value.totalAmount
                val paymentMethod: PaymentMethod = _state.value.paymentMethod

                viewModelScope.launch {
                    itemQuantityList.forEach { (item, quantity) ->
                        vendorRepository.upsertProduct(
                            item.copy(
                                stock = item.stock-quantity
                            )
                        )
                    }
                    Log.i("TransactionViewModel.kt", "itemQuantity=$itemQuantityList\ntotalAmount=$totalAmount\npaymentMethod=$paymentMethod")
                    vendorRepository.insertTransaction(
                        itemQuantityList = itemQuantityList,
                        totalAmount = totalAmount,
                        paymentMethod = paymentMethod
                    )
                }

                _state.update {
                    TransactionState()
                }
            }
        }
    }
}