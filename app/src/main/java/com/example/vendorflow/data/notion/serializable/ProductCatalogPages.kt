package com.example.vendorflow.data.notion.serializable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductCatalogPages(
//    override val `object`: String,
    val results: List<ProductPage>,
//    @SerialName("next_cursor") override val nextCursor: String?,
//    @SerialName("has_more") override val hasMore: Boolean,
//    override val type: String,
//    @SerialName("request_id") override val requestId: String
) : PaginationResponse() {
    @Serializable
    data class ProductPage(
//        var `object`: String,
        var id: String,
        var properties: ProductProperties,
//        var url: String
    ) {
        @Serializable
        data class ProductProperties(
            @SerialName("Name") val name: Title,
            @SerialName("Collection") val collection: Select,
            @SerialName("Stock") val stock: Number,
            @SerialName("Price") val price: Number,
            @SerialName("Cost") val cost: Number
        ) {
            @Serializable
            data class Title(
                val title: List<Text>?
            ) {
                @Serializable
                data class Text(
                    @SerialName("plain_text") val plainText: String,
                )
            }
            @Serializable
            data class Select(
                val select: Option?
            ) {
                @Serializable
                data class Option(
                    val name: String,
                )
            }
            @Serializable
            data class Number(
                val number: Float?
            )
        }
    }
}