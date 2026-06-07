package com.example.bookflow.data.model

data class Book(
    val key: String,
    val title: String,
    val authors: List<String>,
    val publishYear: Int?,
    val cover: BookCover?,
)