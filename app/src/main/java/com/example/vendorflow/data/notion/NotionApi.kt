package com.example.vendorflow.data.notion

import android.util.Log
import com.example.vendorflow.data.notion.serializable.ProductCatalogDatabase
import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import com.example.vendorflow.data.notion.serializable.Users
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class NotionApi(private val client: HttpClient) {

    suspend fun getUsers(): Users {
        return client.get(HttpRoutes.USERS).body<Users>()
    }

    suspend fun getDatabaseTest(): ProductCatalogDatabase {
        return client.get(HttpRoutes.PRODUCT_CATALOG).body<ProductCatalogDatabase>()
    }

    suspend fun getDatabaseQueryTest(): ProductCatalogPages {
        return client.post(HttpRoutes.PRODUCT_CATALOG + "/query").body<ProductCatalogPages>()
    }

    suspend fun getProductCatalogDatabase(): ProductCatalogDatabase {
        val response = client.get(HttpRoutes.PRODUCT_CATALOG)
        Log.i("NotionApi.kt", response.toString())
        return response.body()
    }

    suspend fun queryProductCatalogDatabase(): ProductCatalogPages {
        val response = client.post(HttpRoutes.PRODUCT_CATALOG + "/query")
        Log.i("NotionApi.kt", response.toString())
        Log.i("NotionApi.kt", response.body<String>())
        return response.body<ProductCatalogPages>()
    }

//    suspend fun postPage(): Error {
//        return client.post(HttpRoutes.PRODUCT_CATALOG){
//            parameters {
//                parameter("database_id", PRODUCT_CATALOG_ID)
//                parameter("properties", "")
//            }
//        }.body<Error>()
//    }


    fun Any.prettyPrint(): String {
        val indentSize = 2
        val indent = " ".repeat(indentSize)
        var currentIndentation = 0
        val string = this.toString()
            .replace(", ", ",")
            .replace("=", " = ")
        val stringBuilder = StringBuilder()
        for (char in string) {
            if ("([".contains(char = char)) {
                currentIndentation++
            }
            if (")]".contains(char = char)) {
                currentIndentation--
            }
            if (")]".contains(char = char)) {
                stringBuilder.append("\n${indent.repeat(currentIndentation)}")
            }
            stringBuilder.append(char)
            if (",([".contains(char = char)) {
                stringBuilder.append("\n${indent.repeat(currentIndentation)}")
            }
        }
        return stringBuilder.toString()
    }
}