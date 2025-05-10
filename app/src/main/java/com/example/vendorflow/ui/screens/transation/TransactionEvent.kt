package com.example.vendorflow.ui.screens.transation

import com.example.vendorflow.data.PaymentMethod
import com.example.vendorflow.data.room.entities.Product

sealed interface TransactionEvent {
    data class IncreaseItemQuantity(val product: Product): TransactionEvent
    data class DecreaseItemQuantity(val product: Product): TransactionEvent
    data class DisplayItemizedTransaction(val itemList: Map<Product, Int>): TransactionEvent
    data object CalculateTotal: TransactionEvent
    data class SetPaymentMethod(val paymentMethod: PaymentMethod): TransactionEvent
    data object ProcessTransaction: TransactionEvent

}