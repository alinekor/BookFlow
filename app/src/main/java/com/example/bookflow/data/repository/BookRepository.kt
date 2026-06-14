package com.example.bookflow.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.remote.paging.BookSearchPagingSource
import kotlinx.coroutines.flow.Flow

class BookRepository {

    private val bookNetworkService = BookNetworkService()

    private val pagingConfig = PagingConfig(
        initialLoadSize = SEARCH_BOOKS_INITIAL_SIZE,
        pageSize = SEARCH_BOOKS_PAGE_SIZE,
        enablePlaceholders = false,
    )

    fun searchBooks(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                BookSearchPagingSource(
                    networkService = bookNetworkService,
                    query = query,
                )
            }
        ).flow
    }

    companion object {
        private const val SEARCH_BOOKS_PAGE_SIZE = 20
        private const val SEARCH_BOOKS_INITIAL_SIZE = 30
    }
}