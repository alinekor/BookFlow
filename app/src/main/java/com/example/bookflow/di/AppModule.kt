package com.example.bookflow.di

import android.content.Context
import com.example.bookflow.data.local.LibraryDataSource
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.repository.BookRepository

object AppModule {

    fun init(context: Context) {
        DatabaseModule.init(context)
    }

    private val bookNetworkService: BookNetworkService by lazy {
        BookNetworkService(openLibraryApi = NetworkModule.openLibraryApi)
    }

    private val libraryDataSource: LibraryDataSource by lazy {
        LibraryDataSource(bookDao = DatabaseModule.bookDao)
    }

    val bookRepository: BookRepository by lazy {
        BookRepository(
            bookNetworkService = bookNetworkService,
            libraryDatasource = libraryDataSource,
        )
    }
}