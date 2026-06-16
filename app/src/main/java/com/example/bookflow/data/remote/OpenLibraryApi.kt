package com.example.bookflow.data.remote

import com.example.bookflow.data.remote.dto.BookDetailsNetworkDto
import com.example.bookflow.data.remote.dto.BookSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("/search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i",
    ): BookSearchResponseDto

    @GET("{key}.json")
    suspend fun loadBookDetails(
        @Path("key") bookKey: String,
    ): BookDetailsNetworkDto
}