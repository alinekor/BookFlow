package com.example.bookflow.data.local.mapper

import com.example.bookflow.data.local.entity.BookDbEntity
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.model.BookDetails

object BooksDbMapper {

    fun mapToBookDbEntity(book: BookDetails) = BookDbEntity(
        bookKey = book.key,
        title = book.title,
        description = book.description,
        authors = book.authors,
        subjects = book.subjects,
        publishYear = book.publishYear,
        coverId = book.cover?.id,
    )

    fun mapToBookDetails(entity: BookDbEntity) = BookDetails(
        key = entity.bookKey,
        title = entity.title,
        description = entity.description,
        authors = entity.authors,
        subjects = entity.subjects,
        publishYear = entity.publishYear,
        cover = entity.coverId?.let(::BookCover),
    )

    fun mapToBook(entity: BookDbEntity) = Book(
        key = entity.bookKey,
        title = entity.title,
        authors = entity.authors,
        publishYear = entity.publishYear,
        cover = entity.coverId?.let(::BookCover),
    )
}