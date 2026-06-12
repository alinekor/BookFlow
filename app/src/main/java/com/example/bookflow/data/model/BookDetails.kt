package com.example.bookflow.data.model

data class BookDetails(
    val key: String,
    val title: String,
    val description: String?,
    val authors: List<String>,
    val subjects: List<String>,
    val publishYear: Int?,
    val cover: BookCover?,
)