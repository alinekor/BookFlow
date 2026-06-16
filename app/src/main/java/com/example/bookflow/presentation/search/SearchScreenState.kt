package com.example.bookflow.presentation.search

import com.example.bookflow.data.model.Book

data class SearchScreenState(
    val query: String,
    val isSearchExpanded: Boolean,
    val searchState: SearchState,
) {
    companion object {
        val INITIAL = SearchScreenState(
            query = "",
            isSearchExpanded = false,
            searchState = SearchState.Initial,
        )
    }
}

sealed class SearchState {

    data object Initial : SearchState()

    data object Loading : SearchState()

    data object EmptySearch : SearchState()

    data class Content(
        val books: List<Book>,
    ) : SearchState()

    data object Error : SearchState()
}