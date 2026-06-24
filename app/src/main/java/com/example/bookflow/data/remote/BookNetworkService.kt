package com.example.bookflow.data.remote

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.remote.mapper.BookNetworkMapper
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
        response.docs.orEmpty().map(BookNetworkMapper::map)
    }
}