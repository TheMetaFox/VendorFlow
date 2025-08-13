package com.example.vendorflow.logic

import android.util.Log
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
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
        withContext(context = defaultDispatcher) {
            val productCatalogPages: ProductCatalogPages = notionRepository.getProductCatalogPages()
            productCatalogPages.results.forEach { page ->
                if (getProductUseCase(productName = page.properties.name.title!![0].plainText) == null) {
                    Log.i("SyncAppToNotionUseCase.kt", "${page.properties.name.title!![0].plainText} does not exist in Vendor Flow")
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