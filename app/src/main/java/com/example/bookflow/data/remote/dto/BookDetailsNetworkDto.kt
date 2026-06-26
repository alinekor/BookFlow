package com.example.bookflow.data.remote.dto

import com.example.bookflow.data.remote.searializer.DescriptionDeserializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class BookDetailsNetworkDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    @JsonAdapter(DescriptionDeserializer::class)
    val description: String?,
    @SerializedName("authors")
    val authors: List<AuthorNetworkDto>?,
    @SerializedName("subjects")
    val subjects: List<String>?,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int?,
    @SerializedName("covers")
    val covers: List<Int>?,
)