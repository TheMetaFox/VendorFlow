package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsOrderedByNameUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return vendorRepository.getProductsOrderedByName()
    }
}