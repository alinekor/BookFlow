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
import com.example.bookflow.ui.screens.details.BookDetailsScreen
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
                    onBookClick = {
                        navController.navigate(AppDestination.BookDetails)
                    }
                )
            }
            composable<AppDestination.BookDetails> {
                BookDetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
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