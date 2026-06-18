package com.example.bookflow.data.remote

import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.data.remote.dto.BookDetailsNetworkDto
import com.example.bookflow.data.remote.dto.BookNetworkDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BookNetworkService(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_CONNECT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_READ_WRITE_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_READ_WRITE_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val openLibraryRetrofit = Retrofit.Builder()
        .baseUrl("https://openlibrary.org")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openLibraryApi = openLibraryRetrofit.create(OpenLibraryApi::class.java)

    suspend fun searchBooks(
        query: String,
        nextPage: Int,
        limit: Int
    ): List<Book> = withContext(defaultDispatcher) {
        val response = openLibraryApi.searchBooks(
            query = query,
            page = nextPage,
            limit = limit,
        )
        response.books?.map { it.toDomain() }.orEmpty()
    }

    suspend fun loadBookDetails(bookKey: String): BookDetails = withContext(defaultDispatcher) {
        val bookDetailsDto = openLibraryApi.loadBookDetails(bookKey)

        val authorNames = bookDetailsDto.authors
            ?.map { authorDto -> authorDto.authorKey.key }
            ?.map { authorKey -> openLibraryApi.loadAuthor(authorKey) }
            ?.map { author -> author.name }
            .orEmpty()

        bookDetailsDto.toDomain(authorNames = authorNames)
    }

    private fun BookNetworkDto.toDomain(): Book = Book(
        key = this.key,
        title = this.title,
        authors = this.authors.orEmpty(),
        publishYear = this.publishYear,
        cover = coverId?.let(::BookCover),
    )

    private fun BookDetailsNetworkDto.toDomain(authorNames: List<String>) = BookDetails(
        key = this.key,
        title = this.title,
        description = this.description,
        authors = authorNames,
        subjects = this.subjects.orEmpty(),
        publishYear = this.publishYear,
        cover = covers?.firstOrNull()?.let(::BookCover),
    )

    companion object {
        private const val TIMEOUT_CONNECT_SECONDS = 30L
        private const val TIMEOUT_READ_WRITE_SECONDS = 120L
    }
}