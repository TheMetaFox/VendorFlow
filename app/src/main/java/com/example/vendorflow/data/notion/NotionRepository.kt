package com.example.vendorflow.data.notion

import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import com.example.vendorflow.data.notion.serializable.Users

class NotionRepository(private val notionApi: NotionApi) {

    suspend fun getUsers(): Users {
        return notionApi.getUsers()
    }

    suspend fun getProductCatalogPages(): ProductCatalogPages {
        return notionApi.getDatabaseQueryTest()
    }
}