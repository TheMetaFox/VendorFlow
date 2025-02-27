package com.example.vendorflow.ui.screens.catalog

import android.net.Uri
import com.example.vendorflow.data.SortType
import com.example.vendorflow.data.entities.Product

data class CatalogState(
    val catalogList: List<Product> = listOf(),
    val sortType: SortType = SortType.NAME,
    val isShowingCatalogItemDialog: Boolean = false,
    val productImageUri: Uri? = null,
    val productNameField: String = "",
    val collectionField: String = "",
    val productIdField: String = "",
    val priceField: String = "",
    val costField: String = "",
)