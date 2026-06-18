package com.example.bookflow.data.local

import android.content.Context
import com.example.bookflow.data.local.dao.BookDao
import com.example.bookflow.data.local.entity.BookDbEntity
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.model.BookDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LibraryDataSource(
    context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val libraryDatabase = LibraryDatabase.getInstance(context)
    private val bookDao: BookDao = libraryDatabase.bookDao()

    fun observeAllBooks(): Flow<List<Book>> {
        return bookDao.observeAllBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBookDomain() }
            }
            .flowOn(defaultDispatcher)
    }

    suspend fun getBookByKey(bookKey: String): BookDetails? = withContext(defaultDispatcher) {
        val bookEntity = bookDao.getBookByKey(bookKey)
        bookEntity?.toBookDetailsDomain()
    }

    fun observeIsBookExist(bookKey: String): Flow<Boolean> {
        return bookDao.observeIsBookExist(bookKey)
            .flowOn(defaultDispatcher)
    }

    suspend fun insertBook(book: BookDetails) = withContext(defaultDispatcher) {
        val bookEntity = book.toDbEntity()
        bookDao.insertBook(bookEntity)
    }

    suspend fun deleteBook(bookKey: String) = withContext(defaultDispatcher) {
        bookDao.deleteBook(bookKey)
    }

    private fun BookDetails.toDbEntity() = BookDbEntity(
        bookKey = this.key,
        title = this.title,
        description = this.description,
        authors = this.authors,
        subjects = this.subjects,
        publishYear = this.publishYear,
        coverId = this.cover?.id,
    )

    private fun BookDbEntity.toBookDetailsDomain() = BookDetails(
        key = this.bookKey,
        title = this.title,
        description = this.description,
        authors = this.authors,
        subjects = this.subjects,
        publishYear = this.publishYear,
        cover = this.coverId?.let(::BookCover),
    )

    private fun BookDbEntity.toBookDomain() = Book(
        key = this.bookKey,
        title = this.title,
        authors = this.authors,
        publishYear = this.publishYear,
        cover = this.coverId?.let(::BookCover),
    )
}