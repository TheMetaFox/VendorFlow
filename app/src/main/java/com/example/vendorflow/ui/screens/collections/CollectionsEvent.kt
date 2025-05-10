package com.example.vendorflow.ui.screens.collections

import com.example.vendorflow.data.room.entities.Collection

sealed interface CollectionsEvent {
    data class ShowCollectionDialog(val collection: Collection?): CollectionsEvent
    data object HideCollectionDialog: CollectionsEvent
    data class UpdateCollectionNameField(val text: String): CollectionsEvent
    data object UpsertCollection: CollectionsEvent
    data class DeleteCollection(val collection: Collection): CollectionsEvent
}