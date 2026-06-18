package com.example.bookflow.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookflow.ui.screens.details.BookDetailsNavRouter
import com.example.bookflow.ui.screens.details.BookDetailsScreen
import com.example.bookflow.ui.screens.search.SearchNavRouter
import com.example.bookflow.ui.screens.search.SearchScreen
import com.example.bookflow.ui.screens.shelf.MyShelfNavRouter
import com.example.bookflow.ui.screens.shelf.MyShelfScreen

@Composable
fun AppNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = GraphRoute.SearchGraph,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        navigation<GraphRoute.SearchGraph>(
            startDestination = AppDestination.Search
        ) {
            composable<AppDestination.Search> {
                val router = SearchNavRouter(navController)
                SearchScreen(router)
            }
            composable<AppDestination.BookDetails> {
                val router = BookDetailsNavRouter(navController)
                BookDetailsScreen(router)
            }
        }

        navigation<GraphRoute.MyShelfGraph>(
            startDestination = AppDestination.MyShelf
        ) {
            composable<AppDestination.MyShelf> {
                val router = MyShelfNavRouter(navController)
                MyShelfScreen(router)
            }
            composable<AppDestination.BookDetails> {
                val router = BookDetailsNavRouter(navController)
                BookDetailsScreen(router)
            }
        }
    }
}