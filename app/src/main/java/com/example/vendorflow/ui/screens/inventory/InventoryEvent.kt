package com.example.vendorflow.ui.screens.inventory

import android.content.Context
import android.net.Uri
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.enums.SyncSource
import com.example.vendorflow.data.room.entities.Tag

sealed interface InventoryEvent {
    data class IncreaseProductStock(val product: Product): InventoryEvent
    data class DecreaseProductStock(val product: Product): InventoryEvent
    data class UpdateSearchField(val text: String): InventoryEvent
    data class ShowProductDialog(val product: Product?): InventoryEvent
    data object HideProductDialog: InventoryEvent
    data object UpdateVendorFlow: InventoryEvent
    data object UpdateNotion: InventoryEvent
    data class UpdateTextField(val textField: String, val text: String): InventoryEvent
    data class UpdateImageField(val imageUri: Uri): InventoryEvent
    data class UpdateSelectedTags(val tag: Tag): InventoryEvent
    data class UpsertProductItem(val context: Context): InventoryEvent
    data class DeleteProductItem(val product: Product): InventoryEvent
    data class ShowConfirmationDialog(val source: SyncSource): InventoryEvent
    data object HideConfirmationDialog: InventoryEvent

}