package com.example.bookflow.di

import com.example.bookflow.data.remote.BookNetworkService
import com.example.bookflow.data.repository.BookRepository

object AppModule {

    private val bookNetworkService: BookNetworkService by lazy {
        BookNetworkService(openLibraryApi = NetworkModule.openLibraryApi)
    }

    val bookRepository: BookRepository by lazy {
        BookRepository(bookNetworkService = bookNetworkService)
    }
}