package com.example.bookflow.data.remote.mapper

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.dto.BookDetailsNetworkDto
import com.example.bookflow.data.remote.dto.BookNetworkDto

object BooksNetworkMapper {

    fun mapSearchItem(dto: BookNetworkDto): Book = Book(
        key = dto.key,
        title = dto.title,
        authors = dto.authorsName.orEmpty(),
        publishYear = dto.firstPublishYear,
        cover = dto.coverId?.let(::BookCover),
    )

    fun mapBookDetails(dto: BookDetailsNetworkDto, authorNames: List<String>) = BookDetails(
        key = dto.key,
        title = dto.title,
        description = dto.description,
        authors = authorNames,
        subjects = dto.subjects.orEmpty(),
        publishYear = dto.firstPublishYear,
        cover = dto.covers?.firstOrNull()?.let(::BookCover),
    )
}