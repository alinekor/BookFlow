package com.example.bookflow.data.local

import com.example.bookflow.data.local.dao.BookDao
import com.example.bookflow.data.local.mapper.BooksDbMapper
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LibraryDataSource(
    private val bookDao: BookDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun observeAllBooks(): Flow<List<Book>> {
        return bookDao.observeAllBooks()
            .map { bookEntities ->
                bookEntities.map { BooksDbMapper.mapToBook(it) }
            }
            .flowOn(dispatcher)
    }

    suspend fun getBookByKey(bookKey: String): BookDetails? = withContext(dispatcher) {
        val bookEntity = bookDao.getBookByKey(bookKey)
        bookEntity?.let(BooksDbMapper::mapToBookDetails)
    }

    fun observeIsBookExist(bookKey: String): Flow<Boolean> {
        return bookDao.observeIsBookExist(bookKey)
            .flowOn(dispatcher)
    }

    suspend fun insertBook(book: BookDetails) = withContext(dispatcher) {
        val bookEntity = BooksDbMapper.mapToBookDbEntity(book)
        bookDao.insertBook(bookEntity)
    }

    suspend fun deleteBook(bookKey: String) = withContext(dispatcher) {
        bookDao.deleteBook(bookKey)
    }
}