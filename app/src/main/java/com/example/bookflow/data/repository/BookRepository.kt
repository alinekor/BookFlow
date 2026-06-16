package com.example.bookflow.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.remote.paging.BookSearchPagingSource
import kotlinx.coroutines.flow.Flow

class BookRepository {

    private val bookNetworkService = BookNetworkService()

    private val pagingConfig = PagingConfig(
        pageSize = SEARCH_BOOKS_PAGE_SIZE,
        initialLoadSize = SEARCH_BOOKS_PAGE_SIZE,
        enablePlaceholders = false,
    )

    fun searchBooks(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                BookSearchPagingSource(
                    networkService = bookNetworkService,
                    pageSize = SEARCH_BOOKS_PAGE_SIZE,
                    query = query,
                )
            }
        ).flow
    }

    suspend fun loadBookDetails(bookKey: String): BookDetails {
        return bookNetworkService.loadBookDetails(bookKey)
    }

    companion object {
        private const val SEARCH_BOOKS_PAGE_SIZE = 20
    }
}