package com.example.bookflow.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.bookflow.data.local.LibraryDataSource
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.remote.paging.BookSearchPagingSource
import kotlinx.coroutines.flow.Flow

class BookRepository(context: Context) {

    private val bookNetworkService = BookNetworkService()
    private val libraryDatasource = LibraryDataSource(context)

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

    fun observeLibraryBooks(): Flow<List<Book>> {
        return libraryDatasource.observeAllBooks()
    }

    suspend fun getLibraryBook(bookKey: String): BookDetails? {
        return libraryDatasource.getBookByKey(bookKey)
    }

    fun observeSavedToLibrary(bookKey: String): Flow<Boolean> {
        return libraryDatasource.observeIsBookExist(bookKey)
    }

    suspend fun saveBookToLibrary(book: BookDetails) {
        libraryDatasource.insertBook(book)
    }

    suspend fun deleteBookFromLibrary(bookKey: String) {
        libraryDatasource.deleteBook(bookKey)
    }

    companion object {
        private const val SEARCH_BOOKS_PAGE_SIZE = 20
    }
}