package com.example.vendorflow.ui.screens.catalog

import android.content.Context
import android.net.Uri
import com.example.vendorflow.data.room.entities.Product

sealed interface CatalogEvent {
    data class ShowCatalogItemDialog(val item: Product?): CatalogEvent
    data object HideCatalogItemDialog: CatalogEvent
    data object UpdateVendorFlow: CatalogEvent
    data object UpdateNotion: CatalogEvent
    data class UpdateTextField(val textField: String, val text: String): CatalogEvent
    data class UpdateImageField(val imageUri: Uri): CatalogEvent
    data class UpsertCatalogItem(val context: Context): CatalogEvent
    data class DeleteCatalogItem(val item: Product): CatalogEvent
}