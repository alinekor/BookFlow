package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookDetailsNetworkDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: DescriptionNetworkDto?,
    @SerializedName("authors")
    val authors: List<AuthorDto>?,
    @SerializedName("subjects")
    val subjects: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("covers")
    val covers: List<Int>?,
)

data class DescriptionNetworkDto(
    @SerializedName("value")
    val value: String?
)