package com.example.vendorflow.ui.screens.sales

sealed interface SalesEvent {
    data object LoadSalesData: SalesEvent
}