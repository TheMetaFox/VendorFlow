package com.example.vendorflow.ui.screens.tags

import com.example.vendorflow.data.room.entities.Tag

sealed interface TagsEvent {
    data class ShowTagDialog(val tag: Tag?): TagsEvent
    data object HideTagDialog: TagsEvent
    data class UpdateTagNameField(val text: String): TagsEvent
    data object UpsertTag: TagsEvent
    data class DeleteTag(val tag: Tag): TagsEvent
    data class UpdateOrdinals(val tag: Tag, val ordinal: Int): TagsEvent
}