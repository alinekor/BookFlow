package com.example.bookflow.di

import android.content.Context
import com.example.bookflow.data.local.LibraryDataSource
import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.repository.BookRepository

object AppModule {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    private val bookNetworkService: BookNetworkService by lazy {
        BookNetworkService(openLibraryApi = NetworkModule.openLibraryApi)
    }

    private val libraryDataSource: LibraryDataSource by lazy {
        LibraryDataSource(context = applicationContext)
    }

    val bookRepository: BookRepository by lazy {
        BookRepository(
            bookNetworkService = bookNetworkService,
            libraryDatasource = libraryDataSource,
        )
    }
}
