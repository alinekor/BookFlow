package com.example.bookflow.data.model

data class BookCover(
    val id: Int,
) {
    val mediumUrl: String
        get() = "https://covers.openlibrary.org/b/id/$id-M.jpg"

    val largeUrl: String
        get() = "https://covers.openlibrary.org/b/id/$id-L.jpg"
}