package com.example.vendorflow.ui.screens.inventory

import com.example.vendorflow.data.room.entities.Product

sealed interface InventoryEvent {
    data class IncreaseProductStock(val product: Product): InventoryEvent
    data class DecreaseProductStock(val product: Product): InventoryEvent
}