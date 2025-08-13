package com.example.vendorflow.data.notion.serializable

import com.example.vendorflow.data.enums.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductCatalogDatabase(
    val `object`: String,
    val id: String,
    val properties: Schema
) {
    @Serializable
    data class Schema(
        @SerialName("Name") val name: Title,
        @SerialName("Collection") val collection: Select,
        @SerialName("Materials") val materials: MultiSelect,
        @SerialName("Stock") val stock: Number,
        @SerialName("Cost") val cost: Number,
        @SerialName("Price") val price: Number,
        @SerialName("Profit") val profit: Formula,
        @SerialName("Surcharge") val surcharge: Number,
        @SerialName("Capital") val capital: Formula,
        @SerialName("Tags") val tags: MultiSelect,
        @SerialName("Sold") val sold: Number,
        @SerialName("Posted") val posted: Status
    ) {
        @Serializable
        data class Title(
            val name: String,
            val type: String
        )
        @Serializable
        data class Select(
            val name: String,
            val type: String,
            val select: Options
        ) {
            @Serializable
            data class Options(
                val options: List<Option>
            ) {
                @Serializable
                data class Option(
                    val id: String,
                    val name: String,
                    val color: Color
                )
            }
        }
        @Serializable
        data class MultiSelect(
            val name: String,
            val type: String,
            @SerialName("multi_select") val multiSelect: Options
        ) {
            @Serializable
            data class Options(
                val options: List<Option>
            ) {
                @Serializable
                data class Option(
                    val id: String,
                    val name: String,
                    val color: Color
                )
            }
        }
        @Serializable
        data class Number(
            val name: String,
            val type: String,
            val number: Format
        ) {
            @Serializable
            data class Format(
                val format: String
            )
        }
        @Serializable
        data class Formula(
            val name: String,
            val type: String,
            val formula: Expression
        ) {
            @Serializable
            data class Expression(
                val expression: String
            )
        }
        @Serializable
        data class Status(
            val name: String,
            val type: String,
            val status: Options
        ) {
            @Serializable
            data class Options(
                val options: List<Option>
            ) {
                @Serializable
                data class Option(
                    val id: String,
                    val name: String,
                    val color: Color
                )
            }
        }
    }
}