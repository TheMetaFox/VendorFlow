package com.example.vendorflow.data.notion

import android.util.Log
import com.example.vendorflow.data.notion.serializable.ProductCatalogDatabase
import com.example.vendorflow.data.notion.serializable.ProductCatalogPages
import com.example.vendorflow.data.notion.serializable.Properties
import com.example.vendorflow.data.notion.serializable.Users
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters

class NotionApi(private val client: HttpClient) {

//    private fun parseHttpResponse(response: HttpResponse): HttpResponse  {
//        Log.i("NotionApi.kt", "Response status: ${response.status}")
//        Log.i("NotionApi.kt", "Value: ${response.status.value}")
//        Log.i("NotionApi.kt", "Description: ${response.status.description}")
//        Log.i("NotionApi.kt", "Success: ${response.status.isSuccess()}")
//        val result = if (response.status.isSuccess()) {
//            Result.Success(response).data
//        } else {
//            Result.Error(response)
//        }
//        return response
//    }

    suspend fun getUsers(): Users {
        return client.get(HttpRoutes.USERS).body<Users>()
    }

    suspend fun getDatabaseTest(): ProductCatalogDatabase {
        return client.get(HttpRoutes.PRODUCT_CATALOG).body<ProductCatalogDatabase>()
    }

    suspend fun queryDatabase(): ProductCatalogPages {
        return client.post(HttpRoutes.PRODUCT_CATALOG + "/query").body<ProductCatalogPages>()
    }

    suspend fun updatePageProperties(pageId: String, stock: Int) {
        val response = client.patch(HttpRoutes.BASE_PAGES_URL + pageId) {
            parameters {
                contentType(ContentType.Application.Json)
                setBody(Properties(Properties.Property(Properties.Property.Number(value = stock))))
            }
        }
        Log.i("NotionApi.kt", response.toString() + "\n" + response.body<String>())
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