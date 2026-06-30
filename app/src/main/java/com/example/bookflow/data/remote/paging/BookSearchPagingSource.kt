package com.example.bookflow.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.remote.BookNetworkService
import kotlin.coroutines.cancellation.CancellationException

class BookSearchPagingSource(
    private val networkService: BookNetworkService,
    private val pageSize: Int,
    private val query: String,
) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val nextPage = params.key ?: 1

        return try {
            val books = networkService.searchBooks(
                query = query,
                nextPage = nextPage,
                limit = pageSize,
            )

            LoadResult.Page(
                data = books,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (books.size < pageSize) null else nextPage + 1
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
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