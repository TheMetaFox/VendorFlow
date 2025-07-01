package com.example.vendorflow.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface VendorDao {

    @Insert
    suspend fun insertSale(sale: Sale)

    @Insert
    suspend fun insertSoldItem(soldItem: SoldItem)

    @Upsert
    suspend fun upsertProduct(product: Product)

    @Upsert
    suspend fun upsertCollection(collection: Collection)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM product ORDER BY productId")
    suspend fun getProducts(): List<Product>

    @Query("DELETE FROM collection WHERE collectionId == :collectionId")
    suspend fun deleteCollection(collectionId: Int)

    @Query("DELETE FROM product WHERE productId == :productId")
    suspend fun deleteProduct(productId: Int)

    @Query("SELECT * FROM product ORDER BY productname ASC")
    fun getProductsOrderedByName(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY productId ASC, productName ASC")
    fun getProductsOrderedByBarcode(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY price ASC")
    fun getProductsOrderedByPrice(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY cost ASC")
    fun getProductsOrderedByCost(): Flow<List<Product>>

    @Query("SELECT * FROM collection ORDER BY collectionId ASC")
    fun getCollectionsOrderedById(): Flow<List<Collection>>

    @Query("SELECT * FROM sale ORDER BY datetime DESC")
    suspend fun getSalesOrderedByDateTime(): List<Sale>

    @Query("SELECT * FROM product WHERE productId = :productId")
    suspend fun getProductFromProductId(productId: Int): Product

    @Query("SELECT * FROM product WHERE productName = :productName")
    suspend fun getProductFromProductName(productName: String): Product?

    @Query("SELECT * FROM collection WHERE collectionId = :collectionId")
    suspend fun getCollectionFromCollectionId(collectionId: Int): Collection

    @Query("SELECT * FROM collection WHERE collectionName = :collectionName")
    suspend fun getCollectionFromCollectionName(collectionName: String): Collection

    @Query("SELECT * FROM solditem WHERE saleId = :saleId ORDER BY productname ASC")
    suspend fun getSoldItemsFromSaleId(saleId: Int): List<SoldItem>

    @Query("SELECT collectionName FROM collection WHERE collectionId = :collectionId")
    suspend fun getCollectionNameFromCollectionId(collectionId: Int): String?

    @Query("SELECT collectionId FROM collection WHERE collectionName = :collectionName")
    suspend fun getCollectionIdFromCollectionName(collectionName: String): Int?

    @Query("SELECT stock FROM product WHERE productId = :productId")
    suspend fun getStockFromProductId(productId: Int): Int

    @Query("SELECT SUM(price * stock) FROM product AS totalprice")
    fun getTotalInventoryPrice(): Flow<Float>

    @Query("SELECT SUM(cost * stock) FROM product AS totalprice")
    fun getTotalInventoryCost(): Flow<Float>

    @Query("SELECT saleid FROM sale WHERE datetime = :dateTime")
    suspend fun getSaleIdFromDateTime(dateTime: LocalDateTime): Int

}