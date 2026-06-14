package com.example.bookflow.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.remote.BookNetworkService

class BookSearchPagingSource(
    private val networkService: BookNetworkService,
    private val query: String,
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val nextPage = params.key ?: 1

        return try {
            val books = networkService.searchBooks(
                query = query,
                nextPage = nextPage,
                limit = params.loadSize,
            )

            LoadResult.Page(
                data = books,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (books.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}