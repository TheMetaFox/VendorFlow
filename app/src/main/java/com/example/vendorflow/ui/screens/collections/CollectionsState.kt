package com.example.vendorflow.ui.screens.collections

import com.example.vendorflow.data.room.entities.Collection

data class CollectionsState(
    val collectionList: List<Collection> = listOf(),
    val isShowingCollectionDialog: Boolean = false,
    val collectionId: Int? = null,
    val collectionNameField: String = ""
)
