package com.example.vendorflow.ui.screens.transation

import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Tag

sealed interface TransactionEvent {
    data class UpdateSearchField(val text: String): TransactionEvent
    data class IncreaseItemQuantity(val product: Product): TransactionEvent
    data class DecreaseItemQuantity(val product: Product): TransactionEvent
    data class DisplayItemizedTransaction(val itemList: Map<Product, Int>): TransactionEvent
    data object CalculateTotal: TransactionEvent
    data class SetPaymentMethod(val paymentMethod: PaymentMethod): TransactionEvent
    data object ProcessTransaction: TransactionEvent
    data class UpdateSelectedTags(val tag: Tag): TransactionEvent
    data object LoadProductTagsMap: TransactionEvent
}