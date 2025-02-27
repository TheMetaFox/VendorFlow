package com.example.vendorflow.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vendorflow.data.PaymentMethod
import java.time.LocalDateTime

@Entity
data class Sale(
    @PrimaryKey(autoGenerate = true) val saleId: Int,
    val dateTime: LocalDateTime,
    val amount: Float,
    val paymentMethod: PaymentMethod,
) {
    constructor(dateTime: LocalDateTime, amount: Float, paymentMethod: PaymentMethod) : this (
        saleId = 0, dateTime = dateTime, amount = amount, paymentMethod = paymentMethod
    )
}