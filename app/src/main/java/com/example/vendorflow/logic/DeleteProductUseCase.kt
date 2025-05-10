package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Product
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(product: Product) {
        vendorRepository.deleteProduct(product = product)
    }
}