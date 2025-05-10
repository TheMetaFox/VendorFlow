package com.example.vendorflow.ui.screens.sales

import com.example.vendorflow.data.room.entities.Collection
import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem
import com.example.vendorflow.ui.screens.collections.CollectionsEvent

sealed interface SalesEvent {
    data object LoadSalesData: SalesEvent
    data class ShowSaleDialog(val sale: Sale, val soldItems: List<SoldItem>): SalesEvent
    data object HideSaleDialog: SalesEvent
}