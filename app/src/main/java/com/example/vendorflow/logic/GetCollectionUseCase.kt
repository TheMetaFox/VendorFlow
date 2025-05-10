package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Collection
import javax.inject.Inject

class GetCollectionUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(collectionId: Int): Collection? {
        return vendorRepository.getCollectionFromCollectionId(collectionId = collectionId)
    }
    suspend operator fun invoke(collectionName: String): Collection? {
        return vendorRepository.getCollectionFromCollectionName(collectionName = collectionName)
    }
}