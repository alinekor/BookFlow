package com.example.bookflow.presentation.details

import com.example.bookflow.data.model.Book

sealed class SearchResultState {

    data object Initial : SearchResultState()

    data object Loading : SearchResultState()

    data object EmptySearch : SearchResultState()

    data class Content(
        val books: List<Book>,
    ) : SearchResultState()

    data class Error(
        val errorMessage: String,
    ) : SearchResultState()
}