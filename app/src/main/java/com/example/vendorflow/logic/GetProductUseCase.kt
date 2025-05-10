package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Product
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(productId: Int): Product {
        return vendorRepository.getProductFromProductId(productId = productId)
    }
    suspend operator fun invoke(productName: String): Product? {
        return vendorRepository.getProductFromProductName(productName = productName)
    }
}