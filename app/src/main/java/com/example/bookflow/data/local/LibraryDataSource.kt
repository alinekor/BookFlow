package com.example.bookflow.data.local

import android.content.Context
import androidx.room.Room

class LibraryDataSource(context: Context) {

    private val libraryDatabase = Room.databaseBuilder(
        context, LibraryDatabase::class.java, LIBRARY_DATABASE_NAME
    ).build()

    private val bookDao = libraryDatabase.bookDao()

    companion object {
        private const val LIBRARY_DATABASE_NAME = "library_database"
    }
}