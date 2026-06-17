package com.example.bookflow.presentation.search

import androidx.paging.PagingData
import com.example.bookflow.data.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchScreenState(
    val query: String,
    val isSearchExpanded: Boolean,
    val booksPagingData: Flow<PagingData<Book>>,
) {
    companion object {
        val INITIAL = SearchScreenState(
            query = "",
            isSearchExpanded = false,
            booksPagingData = flowOf(PagingData.empty()),
        )
    }
}