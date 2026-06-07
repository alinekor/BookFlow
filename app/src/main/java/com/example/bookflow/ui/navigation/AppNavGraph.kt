package com.example.bookflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookflow.ui.screens.details.BookDetailsScreen
import com.example.bookflow.ui.screens.search.SearchScreen
import com.example.bookflow.ui.screens.shelf.MyShelfScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppBottomDestination.Search.name,
        modifier = modifier,
    ) {
        composable(route = AppBottomDestination.Search.name) {
            SearchScreen()
        }
        composable(route = AppBottomDestination.MyShelf.name) {
            MyShelfScreen()
        }

        composable<AppDestination.BookDetails> {
            BookDetailsScreen()
        }
    }
}