package com.example.vendorflow.ui.screens.catalog

import android.net.Uri
import com.example.vendorflow.data.room.entities.Product

data class CatalogState(
    val catalogList: List<Product> = listOf(),
    val isShowingCatalogItemDialog: Boolean = false,
    val catalogItemId: Int? = null,
    val productImageUri: Uri = Uri.EMPTY,
    val productNameField: String = "",
    val collectionField: String = "",
    val priceField: String = "",
    val costField: String = "",
)