package com.example.bookflow.data.remote

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.mapper.BooksNetworkMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookNetworkService(
    private val openLibraryApi: OpenLibraryApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun searchBooks(
        query: String,
        nextPage: Int,
        limit: Int
    ): List<Book> = withContext(dispatcher) {
        val response = openLibraryApi.searchBooks(
            query = query,
            page = nextPage,
            limit = limit,
        )
        response.docs.orEmpty().map(BooksNetworkMapper::mapToBook)
    }

    suspend fun loadBookDetails(bookKey: String): BookDetails = withContext(dispatcher) {
        val bookDetailsDto = openLibraryApi.loadBookDetails(bookKey)

        val authorNames = bookDetailsDto.authors
            ?.map { authorDto -> authorDto.author.key }
            ?.map { authorKey -> openLibraryApi.loadAuthor(authorKey) }
            ?.map { author -> author.name }
            .orEmpty()

        BooksNetworkMapper.mapToBookDetails(
            dto = bookDetailsDto,
            authorNames = authorNames,
        )
    }
}