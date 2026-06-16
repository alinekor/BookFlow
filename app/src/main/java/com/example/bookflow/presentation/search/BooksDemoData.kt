package com.example.bookflow.presentation.search

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.model.BookDetails

fun getInitBooks(): List<Book> {
    return List(10) { i ->
        Book(
            key = "OL27448W_$i",
            title = "The Lord of the Rings $i",
            authors = listOf("J. R. R. Tolkien"),
            publishYear = 1954,
            cover = BookCover(id = 8231856),
        )
    }
}

fun getInitBookDetails(): BookDetails {
    return BookDetails(
        key = "OL27448W",
        title = "The Fellowship of the Ring",
        description = """
        In ancient times the Rings of Power were crafted by the Elven-smiths,
        and Sauron forged the One Ring to rule them all. Many years later,
        the fate of Middle-earth rests in the hands of a young hobbit named Frodo.
    """.trimIndent(),
        authors = listOf("J. R. R. Tolkien"),
        subjects = listOf(
            "Fantasy",
            "Adventure",
            "Epic Fantasy",
            "Middle-earth",
            "Quest",
            "Friendship",
            "Magic"
        ),
        publishYear = 1954,
        cover = BookCover(12345),
    )
}