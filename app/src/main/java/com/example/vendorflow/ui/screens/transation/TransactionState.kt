package com.example.vendorflow.ui.screens.transation

import com.example.vendorflow.data.PaymentMethod
import com.example.vendorflow.data.entities.Product

data class TransactionState(
    val productList: List<Product> = listOf(),
    val itemQuantityList: Map<Product,Int> = mapOf(),
    val totalAmount: Float = 0f,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH
    )
