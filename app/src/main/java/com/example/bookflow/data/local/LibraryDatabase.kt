package com.example.bookflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        private const val DATABASE_NAME = "library_database"

        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getInstance(context: Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}