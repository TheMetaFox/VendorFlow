package com.example.vendorflow.logic

import android.util.Log
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Tag
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateTagOrdinalsUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(tag: Tag, newOrdinal: Int) {
        val tagList: List<Tag> = vendorRepository.getTagsOrderedByOrdinal().first()
        Log.i("UpdateTagOrdinalsUseCase.kt", "Tags Before: $tagList")
        val endOrdinal: Int = newOrdinal
        if (tag.ordinal < newOrdinal) {
            val startOrdinal: Int = tag.ordinal+1
            for (ordinal in startOrdinal..endOrdinal) {
                vendorRepository.upsertTag(
                    tag = tagList[ordinal-1].copy(ordinal = ordinal-1)
                )
            }
        }
        if (tag.ordinal > newOrdinal) {
            val startOrdinal: Int = tag.ordinal-1
            for (ordinal in startOrdinal downTo endOrdinal) {
                vendorRepository.upsertTag(
                    tag = tagList[ordinal-1].copy(ordinal = ordinal+1)
                )
            }
        }
        vendorRepository.upsertTag(
            tag = tag.copy(ordinal = newOrdinal)
        )
        Log.i("UpdateTagOrdinalsUseCase.kt", "Tags After: ${vendorRepository.getTagsOrderedByOrdinal().first()}")
    }
}