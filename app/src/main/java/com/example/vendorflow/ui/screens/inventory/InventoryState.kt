package com.example.vendorflow.ui.screens.inventory

import com.example.vendorflow.data.entities.Product

data class InventoryState(
    val inventoryList: List<Product> = listOf(),
    val totalInventoryPrice: Float = 0f,
    val totalInventoryCost: Float = 0f,
)
