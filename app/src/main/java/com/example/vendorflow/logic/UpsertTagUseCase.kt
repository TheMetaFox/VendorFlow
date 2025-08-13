package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Tag
import javax.inject.Inject

class UpsertTagUseCase @Inject constructor(
    private val vendorRepository: VendorRepository,
) {
    suspend operator fun invoke(tag: Tag) {
        vendorRepository.upsertTag(tag = tag)
    }
}