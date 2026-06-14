package com.example.bookflow.data.remote

import com.example.bookflow.data.remote.dto.BookDetailsNetworkDto
import com.example.bookflow.data.remote.dto.BookSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("fields") fields: String = "key,title,author_name,first_publish_year,cover_i",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): BookSearchResponseDto

    @GET("works/{workId}.json")
    suspend fun getBookDetails(
        @Path("workId") workId: String,
    ): BookDetailsNetworkDto
}