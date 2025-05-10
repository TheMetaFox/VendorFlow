package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Collection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsOrderedByIdUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    operator fun invoke(): Flow<List<Collection>> {
        return vendorRepository.getCollectionsOrderedById()
    }
}