package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("author")
    val authorKey: AuthorKeyDto,
)

data class AuthorKeyDto(
    @SerializedName("key")
    val key: String,
)

data class AuthorNameDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
)