package com.example.bookflow.ui.screens.search

import androidx.navigation.NavController
import com.example.bookflow.ui.navigation.AppDestination

interface ISearchRouter {
    fun openBookDetails(bookKey: String)
}

class SearchNavRouter(
    private val navController: NavController,
) : ISearchRouter {

    override fun openBookDetails(bookKey: String) {
        navController.navigate(
            AppDestination.BookDetails(bookKey = bookKey)
        )
    }
}