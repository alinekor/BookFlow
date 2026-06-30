package com.example.bookflow.data.local

import com.example.bookflow.data.model.BookDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class LibraryDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val tempSavedBooks = MutableStateFlow(emptyList<String>())

    fun observeIsBookExist(bookKey: String): Flow<Boolean> {
        return tempSavedBooks.map { savedBooks ->
            bookKey in savedBooks
        }
    }

    suspend fun insertBook(book: BookDetails) = withContext(dispatcher) {
        tempSavedBooks.update { savedBooks ->
            savedBooks + book.key
        }
    }

    suspend fun deleteBook(bookKey: String) = withContext(dispatcher) {
        tempSavedBooks.update { savedBooks ->
            savedBooks - bookKey
        }
    }
}