package com.example.vendorflow.data

import android.util.Log
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem
import com.example.vendorflow.data.room.VendorDao
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

    suspend fun upsertProduct(product: Product) {
        return vendorDao.upsertProduct(product = product)
    }

    suspend fun upsertCollection(collection: Collection) {
        return vendorDao.upsertCollection(collection = collection)
    }

    suspend fun deleteCollection(collection: Collection) {
        return vendorDao.deleteCollection(collectionId = collection.collectionId)
    }

    suspend fun deleteProduct(product: Product) {
        return vendorDao.deleteProduct(productId = product.productId)
    }

    fun getProductsOrderedByName(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByName()
    }

    fun getCollectionsOrderedById(): Flow<List<Collection>> {
        return vendorDao.getCollectionsOrderedById()
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

    suspend fun getCollectionFromCollectionId(collectionId: Int): Collection {
        return vendorDao.getCollectionFromCollectionId(collectionId = collectionId)
    }

    suspend fun getCollectionFromCollectionName(collectionName: String): Collection {
        return vendorDao.getCollectionFromCollectionName(collectionName = collectionName)
    }


//    suspend fun getSoldItemsFromSaleId(saleId: Int): List<Sale> {
//        return vendorDao.getSoldItemsFromSaleId(saleId = saleId)
//    }

}
