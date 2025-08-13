package com.example.vendorflow.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true) val tagId: Int,
    val tagName: String,
    val ordinal: Int,
) {
    constructor(tagName: String, ordinal: Int) : this(
        tagId = 0, tagName = tagName, ordinal = ordinal
    )
}