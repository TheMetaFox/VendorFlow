package com.example.vendorflow.data.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val productId: Int,
    val productName: String,
    val collectionName: String,
    val image: Uri,
    val price: Float,
    val cost: Float,
    val stock: Int
)
