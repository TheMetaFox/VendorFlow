package com.example.vendorflow.ui.screens.sales

import com.example.vendorflow.data.entities.Sale
import com.example.vendorflow.data.entities.SoldItem

data class SalesState(
    val salesList: List<Sale> = listOf(),
    val soldItemsList: List<List<SoldItem>> = listOf()
)