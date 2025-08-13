package com.example.vendorflow.data.room.entities.relations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductTag(
    @PrimaryKey(autoGenerate = true) val productTagId: Int,
    val productId: Int,
    val tagId: Int
) {
    constructor(productId: Int, tagId: Int) : this(
        productTagId = 0, productId = productId, tagId = tagId
    )
}
