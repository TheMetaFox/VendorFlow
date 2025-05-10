package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.relations.SoldItem
import javax.inject.Inject

class GetSoldItemsGroupedBySaleUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(): List<List<SoldItem>> {
        return vendorRepository.getSoldItemsGroupedBySaleOrderedByDateTime()
    }
}