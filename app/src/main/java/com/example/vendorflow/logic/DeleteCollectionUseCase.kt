package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Collection
import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(collection: Collection) {
        vendorRepository.deleteCollection(collection = collection)
    }
}