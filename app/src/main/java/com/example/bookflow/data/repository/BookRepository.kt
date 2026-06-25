package com.example.bookflow.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.remote.paging.BookSearchPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class BookRepository(
    private val bookNetworkService: BookNetworkService,
) {
    private val tempSavedBooks = MutableStateFlow(emptyList<String>())

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

    fun observeSavedToLibrary(bookKey: String): Flow<Boolean> {
        return tempSavedBooks.map { savedBooks ->
            bookKey in savedBooks
        }
    }

    suspend fun saveBookToLibrary(book: BookDetails) = withContext(Dispatchers.IO) {
        tempSavedBooks.update { savedBooks ->
            savedBooks + book.key
        }
    }

    suspend fun removeBookFromLibrary(bookKey: String) = withContext(Dispatchers.IO) {
        tempSavedBooks.update { savedBooks ->
            savedBooks - bookKey
        }
    }

    companion object {
        private const val SEARCH_BOOKS_PAGE_SIZE = 20
    }
}