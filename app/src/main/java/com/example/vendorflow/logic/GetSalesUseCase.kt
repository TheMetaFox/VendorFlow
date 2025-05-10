package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Sale
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(): List<Sale> {
        return vendorRepository.getSalesOrderedByDateTime()
    }
}