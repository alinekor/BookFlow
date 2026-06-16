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
import androidx.navigation.toRoute
import com.example.bookflow.ui.screens.details.BookDetailsNavAction
import com.example.bookflow.ui.screens.details.BookDetailsScreen
import com.example.bookflow.ui.screens.search.SearchNavAction
import com.example.bookflow.ui.screens.search.SearchScreen
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
                SearchScreen(
                    onNavAction = { action ->
                        when (action) {
                            is SearchNavAction.OpenBookDetails ->
                                navController.navigate(AppDestination.BookDetails(bookKey = action.bookKey))
                        }
                    }
                )
            }
            composable<AppDestination.BookDetails> { entry ->
                val route = entry.toRoute<AppDestination.BookDetails>()

                BookDetailsScreen(
                    bookKey = route.bookKey,
                    onNavAction = { action ->
                        when (action) {
                            BookDetailsNavAction.NavigateBack -> navController.popBackStack()
                        }
                    }
                )
            }
        }

        navigation<GraphRoute.MyShelfGraph>(
            startDestination = AppDestination.MyShelf
        ) {
            composable<AppDestination.MyShelf> {
                MyShelfScreen()
            }
        }
    }
}