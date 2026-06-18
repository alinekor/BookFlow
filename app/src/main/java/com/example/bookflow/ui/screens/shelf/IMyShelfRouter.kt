package com.example.bookflow.ui.screens.shelf

import androidx.navigation.NavController
import com.example.bookflow.ui.navigation.AppDestination

interface IMyShelfRouter {
    fun openBookDetails(bookKey: String)
    fun openSearch()
}

class MyShelfNavRouter(
    private val navController: NavController,
) : IMyShelfRouter {

    override fun openBookDetails(bookKey: String) {
        //todo из БД
        navController.navigate(
            AppDestination.BookDetails(bookKey = bookKey)
        )
    }

    override fun openSearch() {
        TODO("Not yet implemented")
    }
}