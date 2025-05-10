package com.example.vendorflow.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Collection(
    @PrimaryKey(autoGenerate = true) val collectionId: Int,
    val collectionName: String,
) {
    constructor(collectionName: String) : this (
        collectionId = 0, collectionName = collectionName
    )
}
