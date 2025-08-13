package com.example.vendorflow.ui.screens.transation

import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.Product

data class TransactionState(
    val tagList: List<Tag> = listOf(),
    val productList: List<Product> = listOf(),
    val searchText: String = "",
    val itemQuantityList: Map<Product,Int> = mapOf(),
    val totalAmount: Float = 0f,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val selectedTags: List<Tag> = listOf(),
    val productTagsMap: Map<Product, List<Tag>> = mapOf()
)