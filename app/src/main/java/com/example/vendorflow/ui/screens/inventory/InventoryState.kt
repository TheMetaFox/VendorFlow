package com.example.vendorflow.ui.screens.inventory

import android.net.Uri
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.enums.SyncSource
import com.example.vendorflow.data.room.entities.Tag

data class InventoryState(
    val inventoryList: List<Product> = listOf(),
    val tagList: List<Tag> = listOf(),
    val totalInventoryPrice: Float = 0f,
    val totalInventoryCost: Float = 0f,
    val searchText: String = "",
    val isShowingConfirmationDialog: Boolean = false,
    val syncSource: SyncSource = SyncSource.VENDOR_FLOW,

    val isShowingProductDialog: Boolean = false,
    val productItemId: Int? = null,
    val productImageUri: Uri = Uri.EMPTY,
    val productNameField: String = "",
    val priceField: String = "",
    val costField: String = "",
    val selectedTags: List<Tag> = listOf()

)
