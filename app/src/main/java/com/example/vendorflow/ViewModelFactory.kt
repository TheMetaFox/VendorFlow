package com.example.vendorflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendorflow.data.VendorRepository
import com.example.vendorflow.ui.screens.catalog.CatalogViewModel
import com.example.vendorflow.ui.screens.inventory.InventoryViewModel
import com.example.vendorflow.ui.screens.login.LoginViewModel
import com.example.vendorflow.ui.screens.sales.SalesViewModel
import com.example.vendorflow.ui.screens.transation.TransactionViewModel

class ViewModelFactory(
    private val vendorRepository: VendorRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel() as T
            }
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                TransactionViewModel(vendorRepository) as T
            }
            modelClass.isAssignableFrom(InventoryViewModel::class.java) -> {
                InventoryViewModel(vendorRepository) as T
            }
            modelClass.isAssignableFrom(CatalogViewModel::class.java) -> {
                CatalogViewModel(vendorRepository) as T
            }
            modelClass.isAssignableFrom(SalesViewModel::class.java) -> {
                SalesViewModel(vendorRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel class was not found.")
        }
    }
}