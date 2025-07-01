package com.example.vendorflow.data.notion.serializable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    @SerialName("properties") val property: Property
) {
    @Serializable
    data class Property(
        @SerialName("Stock") val number: Number
    ) {
        @Serializable
        data class Number(
            @SerialName("number") val value: Int
        )
    }
}
