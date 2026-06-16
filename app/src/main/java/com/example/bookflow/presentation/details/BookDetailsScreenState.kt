package com.example.bookflow.presentation.details

import com.example.bookflow.data.model.BookDetails

sealed class BookDetailsScreenState {
    data object Initial : BookDetailsScreenState()

    data object Loading : BookDetailsScreenState()

    data class Content(
        val bookDetails: BookDetails,
        val savedToLibrary: Boolean,
    ) : BookDetailsScreenState()

    data object Error : BookDetailsScreenState()
}