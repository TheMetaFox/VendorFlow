package com.example.vendorflow.ui.screens.transation

import com.example.vendorflow.data.PaymentMethod
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Product

data class TransactionState(
    val collectionList: List<Collection> = listOf(),
    val productList: List<Product> = listOf(),
    val itemQuantityList: Map<Product,Int> = mapOf(),
    val totalAmount: Float = 0f,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH
)