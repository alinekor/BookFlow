package com.example.bookflow.di

import com.example.bookflow.data.remote.OpenLibraryApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val OPEN_LIBRARY_BASE_URL = "https://openlibrary.org/"
    private const val TIMEOUT_CONNECT_SECONDS = 30L
    private const val TIMEOUT_READ_WRITE_SECONDS = 120L

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_CONNECT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ_WRITE_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_READ_WRITE_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_LIBRARY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val openLibraryApi: OpenLibraryApi by lazy {
        retrofit.create(OpenLibraryApi::class.java)
    }
}