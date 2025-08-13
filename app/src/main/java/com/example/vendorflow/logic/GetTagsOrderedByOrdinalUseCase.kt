package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagsOrderedByOrdinalUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    operator fun invoke(): Flow<List<Tag>> {
        return vendorRepository.getTagsOrderedByOrdinal()
    }
}