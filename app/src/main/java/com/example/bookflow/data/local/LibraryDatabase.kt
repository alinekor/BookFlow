package com.example.bookflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookflow.data.local.converter.DbTypeConverters
import com.example.bookflow.data.local.dao.BookDao
import com.example.bookflow.data.local.entity.BookDbEntity

@Database(
    entities = [BookDbEntity::class],
    version = 1,
)
@TypeConverters(DbTypeConverters::class)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}