package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthorNetworkDto(
    @SerializedName("author")
    val author: AuthorKeyNetworkDto,
)

data class AuthorKeyNetworkDto(
    @SerializedName("key")
    val key: String,
)

data class AuthorNameNetworkDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
)