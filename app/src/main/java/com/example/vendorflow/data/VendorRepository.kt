package com.example.vendorflow.data

import android.util.Log
import com.example.vendorflow.data.enums.PaymentMethod
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem
import com.example.vendorflow.data.room.VendorDao
import com.example.vendorflow.data.room.entities.relations.ProductTag
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class VendorRepository(private val vendorDao: VendorDao) {

    suspend fun insertTransaction(itemQuantityList: Map<Product, Int>, totalAmount: Float, paymentMethod: PaymentMethod) {
        val sale = Sale(dateTime = LocalDateTime.now(), amount = totalAmount, paymentMethod = paymentMethod)
        Log.i("VendorRepository.kt", "Inserting $sale...")
        vendorDao.insertSale(sale)

        val soldItems: List<SoldItem> = itemQuantityList.map(
            transform = { (product, quantity) ->
                SoldItem(saleId = vendorDao.getSaleIdFromDateTime(dateTime = sale.dateTime), productName = product.productName, quantity = quantity)
            }
        )
        soldItems.forEach { soldItem ->
            Log.i("VendorRepository.kt", "Inserting $soldItem...")
            vendorDao.insertSoldItem(soldItem)
        }
    }

    suspend fun getProducts(): List<Product> {
        return vendorDao.getProducts()
    }

    suspend fun upsertProduct(product: Product) {
        return vendorDao.upsertProduct(product = product)
    }

    suspend fun upsertTag(tag: Tag) {
        return vendorDao.upsertTag(tag = tag)
    }

    suspend fun upsertProductTag(productTag: ProductTag) {
        return vendorDao.upsertProductTag(productTag = productTag)
    }

    suspend fun deleteTag(tag: Tag) {
        return vendorDao.deleteTag(tagId = tag.tagId)
    }

    suspend fun deleteProduct(product: Product) {
        return vendorDao.deleteProduct(productId = product.productId)
    }

    suspend fun deleteProductTag(productTag: ProductTag) {
        return vendorDao.deleteProductTag(productId = productTag.productId, tagId = productTag.tagId)
    }


    fun getProductsOrderedByName(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByName()
    }

    fun getTagsOrderedByOrdinal(): Flow<List<Tag>> {
        return vendorDao.getTagsOrderedByOrdinal()
    }


    fun getTotalInventoryPrice(): Flow<Float> {
        return vendorDao.getTotalInventoryPrice()
    }

    fun getTotalInventoryCost(): Flow<Float> {
        return vendorDao.getTotalInventoryCost()
    }

    suspend fun getSalesOrderedByDateTime(): List<Sale> {
        return vendorDao.getSalesOrderedByDateTime()
    }

    suspend fun getSoldItemsGroupedBySaleOrderedByDateTime(): List<List<SoldItem>> {
        return vendorDao.getSalesOrderedByDateTime().map { sale ->
            vendorDao.getSoldItemsFromSaleId(saleId = sale.saleId)
        }
    }

    suspend fun getProductFromProductId(productId: Int): Product {
        return vendorDao.getProductFromProductId(productId = productId)
    }

    suspend fun getProductFromProductName(productName: String): Product? {
        return vendorDao.getProductFromProductName(productName = productName)
    }

    suspend fun getTagFromTagId(tagId: Int): Tag {
        return vendorDao.getTagFromTagId(tagId = tagId)
    }

    suspend fun getTagFromTagName(tagName: String): Tag {
        return vendorDao.getTagFromTagName(tagName = tagName)
    }

    suspend fun getTagsFromProductId(productId: Int): List<Tag> {
        return vendorDao.getTagsFromProductId(productId)
    }

//    suspend fun reorderTags(tag: Tag, ordinal: Int) {
//        return vendorDao.reorderTags(tag = tag, ordinal = ordinal)
//    }


//    suspend fun getSoldItemsFromSaleId(saleId: Int): List<Sale> {
//        return vendorDao.getSoldItemsFromSaleId(saleId = saleId)
//    }

}
