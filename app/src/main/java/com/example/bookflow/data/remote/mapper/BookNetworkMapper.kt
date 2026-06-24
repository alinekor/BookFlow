package com.example.bookflow.data.remote.mapper

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.remote.dto.BookNetworkDto

object BookNetworkMapper {

    fun map(dto: BookNetworkDto): Book = Book(
        key = dto.key,
        title = dto.title,
        authors = dto.authorsName.orEmpty(),
        publishYear = dto.firstPublishYear,
        cover = dto.coverId?.let(::BookCover),
    )
}