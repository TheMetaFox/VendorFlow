package com.example.vendorflow.logic

import android.util.Log
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Tag
import javax.inject.Inject

class GetTagsFromProductIdUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(productId: Int): List<Tag> {
        val tagList: List<Tag> = vendorRepository.getTagsFromProductId(productId = productId)
        Log.i("GetTagsFromProductIdUseCase.kt", tagList.toString())
        return tagList
    }
}