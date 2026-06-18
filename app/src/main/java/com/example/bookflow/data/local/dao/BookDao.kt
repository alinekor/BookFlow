package com.example.bookflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookflow.data.local.entity.BookDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books")
    fun observeAllBooks(): Flow<List<BookDbEntity>>

    @Query("SELECT * FROM books WHERE bookKey = :bookKey LIMIT 1")
    suspend fun getBookByKey(bookKey: String): BookDbEntity?

    @Query("SELECT COUNT(*) > 0 FROM books WHERE bookKey = :bookKey")
    fun observeIsBookExist(bookKey: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookDbEntity)

    @Query("DELETE FROM books WHERE bookKey = :bookKey")
    suspend fun deleteBook(bookKey: String)
}