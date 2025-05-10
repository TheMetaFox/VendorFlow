package com.example.vendorflow.data.room.entities.relations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductCollection(
    @PrimaryKey(autoGenerate = true) val productCollectionId: Int,
    val productId: Int,
    val collectionId: Int
) {
    constructor(productId: Int, collectionId: Int) : this (
        productCollectionId = 0, productId = productId, collectionId = collectionId
    )
}
