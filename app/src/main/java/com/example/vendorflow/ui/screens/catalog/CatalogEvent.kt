package com.example.vendorflow.ui.screens.catalog

import android.content.Context
import android.net.Uri

sealed interface CatalogEvent {
    data object ShowCatalogItemDialog: CatalogEvent
    data object HideCatalogItemDialog: CatalogEvent
    data class UpdateTextField(val textField: String, val text: String): CatalogEvent
    data class UpdateImageField(val imageUri: Uri?): CatalogEvent
    data class UpsertCatalogItem(val context: Context): CatalogEvent
}