package com.example.vendorflow.ui.screens.sales

import com.example.vendorflow.data.room.entities.Sale
import com.example.vendorflow.data.room.entities.relations.SoldItem

data class SalesState(
    val salesList: List<Sale> = listOf(),
    val soldItemsList: List<List<SoldItem>> = listOf(),
    val isShowingSaleDialog: Boolean = false,
    val sale: Sale? = null,
    val soldItems: List<SoldItem> = listOf()
    )