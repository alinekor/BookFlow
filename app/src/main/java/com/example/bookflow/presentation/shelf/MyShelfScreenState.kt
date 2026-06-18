package com.example.bookflow.presentation.shelf

import com.example.bookflow.data.model.Book

sealed class MyShelfScreenState {
    data object Initial : MyShelfScreenState()

    data object Loading : MyShelfScreenState()

    data object Empty : MyShelfScreenState()

    data class Content(
        val books: List<Book>,
    ) : MyShelfScreenState()

    data object Error : MyShelfScreenState()
}