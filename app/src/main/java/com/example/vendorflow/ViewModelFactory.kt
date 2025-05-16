package com.example.vendorflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.logic.DeleteCollectionUseCase
import com.example.vendorflow.logic.DeleteProductUseCase
import com.example.vendorflow.logic.GetCollectionUseCase
import com.example.vendorflow.logic.GetCollectionsOrderedByIdUseCase
import com.example.vendorflow.logic.GetInventoryCostUseCase
import com.example.vendorflow.logic.GetInventoryPriceUseCase
import com.example.vendorflow.logic.GetProductUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.GetSalesUseCase
import com.example.vendorflow.logic.GetSoldItemsGroupedBySaleUseCase
import com.example.vendorflow.logic.InsertTransactionUseCase
import com.example.vendorflow.logic.SyncAppToNotionUseCase
import com.example.vendorflow.logic.UpsertCollectionUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import com.example.vendorflow.ui.screens.catalog.CatalogViewModel
import com.example.vendorflow.ui.screens.collections.CollectionsViewModel
import com.example.vendorflow.ui.screens.inventory.InventoryViewModel
import com.example.vendorflow.ui.screens.login.LoginViewModel
import com.example.vendorflow.ui.screens.sales.SalesViewModel
import com.example.vendorflow.ui.screens.transation.TransactionViewModel

class ViewModelFactory(
    private val vendorRepository: VendorRepository,
    private val notionRepository: NotionRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel() as T
            }
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                TransactionViewModel(
                    UpsertProductUseCase(vendorRepository),
                    InsertTransactionUseCase(vendorRepository),
                    GetCollectionsOrderedByIdUseCase(vendorRepository),
                    GetProductsOrderedByNameUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(InventoryViewModel::class.java) -> {
                InventoryViewModel(
                    UpsertProductUseCase(vendorRepository),
                    GetProductsOrderedByNameUseCase(vendorRepository),
                    GetInventoryPriceUseCase(vendorRepository),
                    GetInventoryCostUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(CatalogViewModel::class.java) -> {
                CatalogViewModel(
                    UpsertProductUseCase(vendorRepository),
                    DeleteProductUseCase(vendorRepository),
                    SyncAppToNotionUseCase(vendorRepository, notionRepository, GetProductUseCase(vendorRepository)),
                    GetProductsOrderedByNameUseCase(vendorRepository),
                    GetProductUseCase(vendorRepository),
                    GetCollectionUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(CollectionsViewModel::class.java) -> {
                CollectionsViewModel(
                    UpsertCollectionUseCase(vendorRepository),
                    DeleteCollectionUseCase(vendorRepository),
                    GetCollectionsOrderedByIdUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(SalesViewModel::class.java) -> {
                SalesViewModel(
                    GetSalesUseCase(vendorRepository),
                    GetSoldItemsGroupedBySaleUseCase(vendorRepository)
                ) as T
            }
            else -> throw IllegalArgumentException("ViewModel class was not found.")
        }
    }
}