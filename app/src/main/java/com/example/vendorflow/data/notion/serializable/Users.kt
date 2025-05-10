package com.example.vendorflow.data.notion.serializable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Users(
//    override val `object`: String,
    @SerialName("results") val results: List<User>,
//    @SerialName("next_cursor") override val nextCursor: String?,
//    @SerialName("has_more") override val hasMore: Boolean,
//    override val type: String,
//    @SerialName("request_id") override val requestId: String
) : PaginationResponse() {
    @Serializable
    data class User(
//        val `object`: String,
//        val id: String,
        val type: String,
        val name: String,
//        @SerialName("avatar_url") val avatarUrl: String
    )
}