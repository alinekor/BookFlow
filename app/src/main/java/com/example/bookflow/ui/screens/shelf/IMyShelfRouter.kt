package com.example.bookflow.ui.screens.shelf

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bookflow.ui.navigation.AppDestination
import com.example.bookflow.ui.navigation.GraphRoute

interface IMyShelfRouter {
    fun openBookDetails(bookKey: String)
    fun openSearch()
}

class MyShelfNavRouter(
    private val navController: NavController,
) : IMyShelfRouter {

    override fun openBookDetails(bookKey: String) {
        navController.navigate(
            AppDestination.BookDetails(bookKey = bookKey)
        )
    }

    override fun openSearch() {
        navController.navigate(GraphRoute.SearchGraph) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
}