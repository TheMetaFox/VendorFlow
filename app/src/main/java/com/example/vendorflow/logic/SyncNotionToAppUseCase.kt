package com.example.vendorflow.logic

import android.util.Log
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncNotionToAppUseCase @Inject constructor(
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
                    Log.i("SyncNotionToAppUseCase.kt", "${page.properties.name.title!![0].plainText} does not exist in Vendor Flow")
                    return@forEach
                }
                notionRepository.updatePageProperties(pageId = page.id, stock = getProductUseCase(productName = page.properties.name.title!![0].plainText)!!.stock)//(pageId = "1b26e753951b803d9ed8efa25ec0a46b", stock = 69)
            }
        }
    }
}