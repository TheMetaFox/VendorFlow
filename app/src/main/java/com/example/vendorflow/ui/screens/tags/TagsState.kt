package com.example.vendorflow.ui.screens.tags

import com.example.vendorflow.data.room.entities.Tag

data class TagsState(
    val tagList: List<Tag> = listOf(),
    val isShowingTagDialog: Boolean = false,
    val tagId: Int? = null,
    val tagNameField: String = ""
)
