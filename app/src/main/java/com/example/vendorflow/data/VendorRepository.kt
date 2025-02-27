package com.example.vendorflow.data

import android.util.Log
import com.example.vendorflow.data.entities.Product
import com.example.vendorflow.data.entities.Sale
import com.example.vendorflow.data.entities.SoldItem
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

    suspend fun upsertProduct(product: Product): Void {
        return vendorDao.upsertProduct(product = product)
    }

    suspend fun increaseProductStock(product: Product): Void {
        return vendorDao.updateProduct(product = product.copy(stock = product.stock+1))
    }

    suspend fun decreaseProductStock(product: Product): Void {
        return vendorDao.updateProduct(product = product.copy(stock = product.stock-1))
    }

    fun getProductsOrderedByName(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByName()
    }

    fun getProductsOrderedByBarcode(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByBarcode()
    }

    fun getProductsOrderedByPrice(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByPrice()
    }

    fun getProductsOrderedByCost(): Flow<List<Product>> {
        return vendorDao.getProductsOrderedByCost()
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


//    suspend fun getSoldItemsFromSaleId(saleId: Int): List<Sale> {
//        return vendorDao.getSoldItemsFromSaleId(saleId = saleId)
//    }

}
