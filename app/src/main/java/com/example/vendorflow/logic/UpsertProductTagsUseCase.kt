package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.relations.ProductTag
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpsertProductTagsUseCase @Inject constructor(
    private val vendorRepository: VendorRepository
) {
    suspend operator fun invoke(product: Product, selectedTags: List<Tag>) {
        vendorRepository.getTagsOrderedByOrdinal().first().forEach { tag ->
            if (selectedTags.contains(element = tag)) {
                vendorRepository.upsertProductTag(
                    productTag = ProductTag(
                        productId = product.productId,
                        tagId = tag.tagId
                    )
                )
            } else {
                vendorRepository.deleteProductTag(
                    productTag = ProductTag(
                        productId = product.productId,
                        tagId = tag.tagId
                    )
                )
            }
        }
    }
}