package com.example.vendorflow.data.room.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val productId: Int,
    val productName: String,
    val collectionId: Int,
    val image: Uri,
    val price: Float,
    val cost: Float,
    val stock: Int
) {
    constructor(productName: String, collectionId: Int, image: Uri, price: Float, cost: Float, stock: Int) : this (
        productId = 0, productName = productName, collectionId = collectionId, image = image, price = price, cost = cost, stock = stock
    )
    constructor(productName: String, image: Uri, price: Float, cost: Float, stock: Int) : this (
        productId = 0, productName = productName, collectionId = 0, image = image, price = price, cost = cost, stock = stock
    )
}