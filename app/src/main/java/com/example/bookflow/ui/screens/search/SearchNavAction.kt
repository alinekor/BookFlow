package com.example.bookflow.ui.screens.search

sealed class SearchNavAction {

    data class OpenBookDetails(
        val bookKey: String
    ) : SearchNavAction()
}