package com.example.bookflow.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookSearchResponseDto(
    @SerializedName("docs")
    val books: List<BookNetworkDto>?,
)