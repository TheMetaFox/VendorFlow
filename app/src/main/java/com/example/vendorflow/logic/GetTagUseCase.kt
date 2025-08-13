package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Tag
import javax.inject.Inject

class GetTagUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(tagId: Int): Tag? {
        return vendorRepository.getTagFromTagId(tagId = tagId)
    }
    suspend operator fun invoke(tagName: String): Tag? {
        return vendorRepository.getTagFromTagName(tagName = tagName)
    }
}