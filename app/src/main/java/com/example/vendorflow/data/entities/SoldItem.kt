package com.example.vendorflow.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SoldItem(
    @PrimaryKey(autoGenerate = true) val soldItemId: Int,
    val saleId: Int,
    val productName: String,
    val quantity: Int
) {
    constructor(saleId: Int, productName: String, quantity: Int) : this(
        soldItemId = 0, saleId = saleId, productName = productName, quantity = quantity
    )
}