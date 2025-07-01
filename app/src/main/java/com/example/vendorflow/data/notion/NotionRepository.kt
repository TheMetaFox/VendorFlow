package com.example.vendorflow.data.notion

import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import com.example.vendorflow.data.notion.serializable.Users

class NotionRepository(private val notionApi: NotionApi) {

    suspend fun getUsers(): Users {
        return notionApi.getUsers()
    }

    suspend fun getProductCatalogPages(): ProductCatalogPages {
        return notionApi.queryDatabase()
    }

    suspend fun updatePageProperties(pageId: String, stock: Int) {
        return notionApi.updatePageProperties(pageId = pageId, stock = stock)
    }
}