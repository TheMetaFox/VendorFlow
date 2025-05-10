package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInventoryPriceUseCase @Inject constructor(
    private val vendorRepository: VendorRepository,
) {
    operator fun invoke(): Flow<Float> {
        return vendorRepository.getTotalInventoryPrice()
    }
}