package com.example.vendorflow.ui.screens.transation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.logic.GetTagsOrderedByOrdinalUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.GetTagsFromProductIdUseCase
import com.example.vendorflow.logic.InsertTransactionUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class TransactionViewModel(
    private val upsertProductUseCase: UpsertProductUseCase,
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val getTagsFromProductIdUseCase: GetTagsFromProductIdUseCase,
    getTagsOrderedByOrdinalUseCase: GetTagsOrderedByOrdinalUseCase,
    private val getProductsOrderedByNameUseCase: GetProductsOrderedByNameUseCase
): ViewModel() {

    private val _tagList: StateFlow<List<Tag>> = getTagsOrderedByOrdinalUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _productList: StateFlow<List<Product>> = getProductsOrderedByNameUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state: MutableStateFlow<TransactionState> = MutableStateFlow(value = TransactionState())
    open val state: StateFlow<TransactionState> = combine(
        flow = _state,
        flow2 = _tagList,
        flow3 = _productList
        ,
        transform = { state, tagList, productList ->
            state.copy(
                tagList = tagList,
                productList = productList
                    .filter {
                        state.searchText.isBlank() or it.productName.contains(other = state.searchText, ignoreCase = true)
                    }
                    .filter { product ->
                        if (state.selectedTags.isEmpty()) return@filter true
                        val productTags: List<Tag>? = state.productTagsMap.get(key = product)
                        if (productTags == null) return@filter true
                        return@filter state.selectedTags.all { tag ->
                            productTags.contains(element = tag)
                        }
                    }
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
            is TransactionEvent.UpdateSearchField -> {
                _state.update {
                    it.copy(
                        searchText = transactionEvent.text
                    )
                }
            }
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
                if (_state.value.itemQuantityList.isEmpty()) {
                    Log.i("TransactionViewModel.kt", "No items selected for transaction")
                    return
                }

                val itemQuantityList: Map<Product, Int> = _state.value.itemQuantityList
                val totalAmount: Float = _state.value.totalAmount
                val paymentMethod: PaymentMethod = _state.value.paymentMethod

                viewModelScope.launch {
                    itemQuantityList.forEach { (item, quantity) ->
//                        vendorRepository.upsertProduct(
//                            item.copy(
//                                stock = item.stock-quantity
//                            )
//                        )
                        upsertProductUseCase(
                            product = item.copy(
                                stock = item.stock-quantity
                            )
                        )
                    }
                    Log.i("TransactionViewModel.kt", "itemQuantity=$itemQuantityList\ntotalAmount=$totalAmount\npaymentMethod=$paymentMethod")
                    insertTransactionUseCase(
                        itemQuantityList = itemQuantityList,
                        totalAmount = totalAmount,
                        paymentMethod = paymentMethod
                    )
                }

                _state.update {
                    TransactionState()
                }
            }
            is TransactionEvent.UpdateSelectedTags -> {
                val selectedTags: List<Tag> = _state.value.selectedTags.toList()
                _state.update {
                    it.copy(
                        selectedTags = if (!selectedTags.contains(element = transactionEvent.tag)) selectedTags.plus(element = transactionEvent.tag) else selectedTags.minus(element = transactionEvent.tag)
                    )
                }
            }
            is TransactionEvent.LoadProductTagsMap -> {
                viewModelScope.launch {
                    val productTagsMap: MutableMap<Product, List<Tag>> = mutableMapOf()
                    getProductsOrderedByNameUseCase().first().forEach { product ->
                        val productTags = getTagsFromProductIdUseCase(productId = product.productId)
                        Log.i("TransactionViewModel.kt", "Product: ${product.productId}  Tags: $productTags")
                        productTagsMap.put(key = product, value = productTags)
                    }
                    Log.i("TransactionViewModel.kt", "Product Tag Map: $productTagsMap")
                    _state.update {
                        it.copy(
                            productTagsMap = productTagsMap
                        )
                    }
                }
            }
        }
    }
}