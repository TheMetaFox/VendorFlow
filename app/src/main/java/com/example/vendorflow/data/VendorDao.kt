package com.example.vendorflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.vendorflow.data.entities.Product
import com.example.vendorflow.data.entities.Sale
import com.example.vendorflow.data.entities.SoldItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface VendorDao {

    @Insert
    suspend fun insertSale(sale: Sale): Void

    @Insert
    suspend fun insertSoldItem(soldItem: SoldItem): Void

    @Upsert
    suspend fun upsertProduct(product: Product): Void

    @Update
    suspend fun updateProduct(product: Product): Void

    @Query("SELECT * FROM product ORDER BY productname ASC")
    fun getProductsOrderedByName(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY productId ASC")
    fun getProductsOrderedByBarcode(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY price ASC")
    fun getProductsOrderedByPrice(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY cost ASC")
    fun getProductsOrderedByCost(): Flow<List<Product>>

    @Query("SELECT * FROM sale ORDER BY datetime DESC")
    suspend fun getSalesOrderedByDateTime(): List<Sale>

    @Query("SELECT * FROM solditem WHERE saleId = :saleId ORDER BY productname ASC")
    suspend fun getSoldItemsFromSaleId(saleId: Int): List<SoldItem>

    @Query("SELECT SUM(price * stock) FROM product AS totalprice")
    fun getTotalInventoryPrice(): Flow<Float>

    @Query("SELECT SUM(cost * stock) FROM product AS totalprice")
    fun getTotalInventoryCost(): Flow<Float>

    @Query("SELECT saleid FROM sale WHERE datetime = :dateTime")
    suspend fun getSaleIdFromDateTime(dateTime: LocalDateTime): Int

}