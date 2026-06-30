package com.example.bookflow

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookflow.ui.components.nav_bar.AppBottomBar
import com.example.bookflow.ui.extensions.getAvailableBottomBarItems
import com.example.bookflow.ui.extensions.isInHierarchy
import com.example.bookflow.ui.navigation.AppNavGraph
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookFlowApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarItems = remember { getAvailableBottomBarItems() }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = bottomBarItems,
                isItemSelected = { navItem ->
                    currentDestination.isInHierarchy(navItem.graphRoute::class)
                },
                onItemClick = { navItem ->
                    val isSameTab = currentDestination.isInHierarchy(navItem.graphRoute::class)

                    if (!isSameTab) {
                        navController.navigate(navItem.graphRoute) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom),
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            paddingValues = innerPadding,
        )
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