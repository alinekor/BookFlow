package com.example.bookflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookflow.ui.components.AppBottomBar
import com.example.bookflow.ui.extensions.toBottomBarItems
import com.example.bookflow.ui.navigation.AppBottomDestination
import com.example.bookflow.ui.navigation.AppNavGraph
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookFlowApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = AppBottomDestination.entries.toList().toBottomBarItems(),
                isItemSelected = { navItem ->
                    currentDestination?.hierarchy?.any { it.route == navItem.route } == true
                },
                onItemClick = { navItem ->
                    if (currentDestination?.route == navItem.route) return@AppBottomBar

                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        AppNavGraph(navController, Modifier.padding(innerPadding))
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookFlowAppPreview() {
    BookFlowTheme {
        BookFlowApp()
    }
}