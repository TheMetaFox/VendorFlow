package com.example.vendorflow.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.vendorflow.data.room.entities.Tag
import com.example.vendorflow.data.room.entities.Product
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.ProductTag
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
    suspend fun upsertTag(tag: Tag)

    @Upsert
    suspend fun upsertProductTag(productTag: ProductTag)

    @Update
    suspend fun updateProduct(product: Product)

    @Update
    suspend fun updateTag(tag: Tag)

    @Query("SELECT * FROM product ORDER BY productId")
    suspend fun getProducts(): List<Product>

    @Query("DELETE FROM tag WHERE tagId == :tagId")
    suspend fun deleteTag(tagId: Int)

    @Query("DELETE FROM product WHERE productId == :productId")
    suspend fun deleteProduct(productId: Int)

    @Query(value = "DELETE FROM productTag WHERE productId == :productId AND tagId == :tagId")
    suspend fun deleteProductTag(productId: Int, tagId: Int)

    @Query("SELECT * FROM product ORDER BY productname ASC")
    fun getProductsOrderedByName(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY productId ASC, productName ASC")
    fun getProductsOrderedByBarcode(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY price ASC")
    fun getProductsOrderedByPrice(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY cost ASC")
    fun getProductsOrderedByCost(): Flow<List<Product>>

    @Query("SELECT * FROM tag ORDER BY ordinal ASC")
    fun getTagsOrderedByOrdinal(): Flow<List<Tag>>

    @Query("SELECT * FROM sale ORDER BY datetime DESC")
    suspend fun getSalesOrderedByDateTime(): List<Sale>

    @Query("SELECT * FROM product WHERE productId = :productId")
    suspend fun getProductFromProductId(productId: Int): Product

    @Query("SELECT * FROM product WHERE productName = :productName")
    suspend fun getProductFromProductName(productName: String): Product?

    @Query("SELECT * FROM tag WHERE tagId = :tagId")
    suspend fun getTagFromTagId(tagId: Int): Tag

    @Query("SELECT * FROM tag WHERE tagName = :tagName")
    suspend fun getTagFromTagName(tagName: String): Tag

    @Query(value = "SELECT * FROM tag JOIN productTag ON tag.tagId == productTag.tagId WHERE productId == :productId ORDER BY ordinal ASC")
    suspend fun getTagsFromProductId(productId: Int): List<Tag>

    @Query("SELECT * FROM solditem WHERE saleId = :saleId ORDER BY productname ASC")
    suspend fun getSoldItemsFromSaleId(saleId: Int): List<SoldItem>

    @Query("SELECT tagName FROM tag WHERE tagId = :tagId")
    suspend fun getTagNameFromTagId(tagId: Int): String?

    @Query("SELECT tagId FROM tag WHERE tagName = :tagName")
    suspend fun getTagIdFromTagName(tagName: String): Int?

    @Query("SELECT stock FROM product WHERE productId = :productId")
    suspend fun getStockFromProductId(productId: Int): Int

    @Query("SELECT SUM(price * stock) FROM product AS totalprice")
    fun getTotalInventoryPrice(): Flow<Float>

    @Query("SELECT SUM(cost * stock) FROM product AS totalprice")
    fun getTotalInventoryCost(): Flow<Float>

    @Query("SELECT saleid FROM sale WHERE datetime = :dateTime")
    suspend fun getSaleIdFromDateTime(dateTime: LocalDateTime): Int

}