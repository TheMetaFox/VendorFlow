package com.example.vendorflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.data.notion.NotionRepository
import com.example.vendorflow.logic.DeleteTagUseCase
import com.example.vendorflow.logic.DeleteProductUseCase
import com.example.vendorflow.logic.GetTagsOrderedByOrdinalUseCase
import com.example.vendorflow.logic.GetInventoryCostUseCase
import com.example.vendorflow.logic.GetInventoryPriceUseCase
import com.example.vendorflow.logic.GetProductUseCase
import com.example.vendorflow.logic.GetProductsOrderedByNameUseCase
import com.example.vendorflow.logic.GetSalesUseCase
import com.example.vendorflow.logic.GetSoldItemsGroupedBySaleUseCase
import com.example.vendorflow.logic.GetTagsFromProductIdUseCase
import com.example.vendorflow.logic.InsertTransactionUseCase
import com.example.vendorflow.logic.SyncAppToNotionUseCase
import com.example.vendorflow.logic.SyncNotionToAppUseCase
import com.example.vendorflow.logic.UpdateTagOrdinalsUseCase
import com.example.vendorflow.logic.UpsertProductTagsUseCase
import com.example.vendorflow.logic.UpsertTagUseCase
import com.example.vendorflow.logic.UpsertProductUseCase
import com.example.vendorflow.ui.screens.tags.TagsViewModel
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
                    GetTagsFromProductIdUseCase(vendorRepository),
                    GetTagsOrderedByOrdinalUseCase(vendorRepository),
                    GetProductsOrderedByNameUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(InventoryViewModel::class.java) -> {
                InventoryViewModel(
                    GetProductsOrderedByNameUseCase(vendorRepository),
                    GetInventoryPriceUseCase(vendorRepository),
                    GetInventoryCostUseCase(vendorRepository),
                    GetTagsOrderedByOrdinalUseCase(vendorRepository),
                    UpsertProductUseCase(vendorRepository),
                    UpsertProductTagsUseCase(vendorRepository),
                    DeleteProductUseCase(vendorRepository),
                    SyncAppToNotionUseCase(vendorRepository, notionRepository, GetProductUseCase(vendorRepository)),
                    SyncNotionToAppUseCase(vendorRepository, notionRepository, GetProductUseCase(vendorRepository)),
                    GetProductUseCase(vendorRepository),
                    GetTagsFromProductIdUseCase(vendorRepository)
                ) as T
            }
            modelClass.isAssignableFrom(TagsViewModel::class.java) -> {
                TagsViewModel(
                    UpsertTagUseCase(vendorRepository),
                    DeleteTagUseCase(vendorRepository),
                    GetTagsOrderedByOrdinalUseCase(vendorRepository),
                    UpdateTagOrdinalsUseCase(vendorRepository)
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