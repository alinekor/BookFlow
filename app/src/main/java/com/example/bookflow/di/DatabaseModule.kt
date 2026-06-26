package com.example.bookflow.di

import android.content.Context
import com.example.bookflow.data.local.LibraryDatabase
import com.example.bookflow.data.local.dao.BookDao

object DatabaseModule {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    private val libraryDatabase: LibraryDatabase by lazy {
        LibraryDatabase.create(context = applicationContext)
    }

    val bookDao: BookDao by lazy { libraryDatabase.bookDao() }
}