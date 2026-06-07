package com.example.bookflow.data.model

data class BookCover(
    val id: Int,
) {
    val smallUrl: String
        get() = "https://covers.openlibrary.org/b/id/$id-S.jpg"

    val mediumUrl: String
        get() = "https://covers.openlibrary.org/b/id/$id-M.jpg"
}