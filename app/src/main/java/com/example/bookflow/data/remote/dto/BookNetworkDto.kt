package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookNetworkDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val authorsName: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("cover_i")
    val coverId: Int?,
)