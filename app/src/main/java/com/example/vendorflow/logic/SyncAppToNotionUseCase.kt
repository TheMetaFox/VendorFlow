package com.example.vendorflow.logic

import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import com.example.vendorflow.data.room.entities.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncAppToNotionUseCase @Inject constructor(
    val vendorRepository: VendorRepository,
    private val notionRepository: NotionRepository,
    val getProductUseCase: GetProductUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke() {
        withContext(defaultDispatcher) {
            val productCatalogPages: ProductCatalogPages = notionRepository.getProductCatalogPages()
            productCatalogPages.results.forEach { page ->
                if (getProductUseCase(productName = page.properties.name.title!![0].plainText) == null) {
                    return@forEach
                }
                vendorRepository.upsertProduct(
                    product = getProductUseCase(productName = page.properties.name.title!![0].plainText)!!.copy(
                        price = page.properties.price.number!!,
                        cost = page.properties.cost.number!!,
                        stock = page.properties.stock.number!!.toInt()
                    )
                )
            }
        }
    }
}