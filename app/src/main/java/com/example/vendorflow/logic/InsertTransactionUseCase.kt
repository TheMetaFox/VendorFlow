package com.example.vendorflow.logic

import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Product
import javax.inject.Inject

class InsertTransactionUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(itemQuantityList: Map<Product, Int>, totalAmount: Float, paymentMethod: PaymentMethod) {
        vendorRepository.insertTransaction(itemQuantityList = itemQuantityList, totalAmount = totalAmount, paymentMethod = paymentMethod)
    }
}