package com.example.vendorflow.data.notion.serializable

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val `object`: String,
    val status: Int,
    val code: String,
    val message: String
)