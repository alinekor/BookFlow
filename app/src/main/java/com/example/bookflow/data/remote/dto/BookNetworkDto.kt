package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookNetworkDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val authors: List<String>?,
    @SerializedName("first_publish_year")
    val publishYear: Int?,
    @SerializedName("cover_i")
    val coverId: Int?,
)