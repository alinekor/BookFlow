package com.example.bookflow.presentation.details

import com.example.bookflow.data.model.Book

sealed class SearchUiState {

    data object Initial : SearchUiState()

    data object Loading : SearchUiState()

    data object EmptySearch : SearchUiState()

    data class Content(
        val books: List<Book>,
    ) : SearchUiState()

    data class Error(
        val errorMessage: String,
    ) : SearchUiState()
}