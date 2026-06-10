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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookflow.ui.components.AppBottomBar
import com.example.bookflow.ui.extensions.getAvailableBottomBarItems
import com.example.bookflow.ui.navigation.AppBottomDestination
import com.example.bookflow.ui.navigation.AppNavGraph
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookFlowApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarItems = remember { getAvailableBottomBarItems() }
    var selectedBottomRoute by rememberSaveable {
        mutableStateOf(AppBottomDestination.Search.name)
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = bottomBarItems,
                isItemSelected = { navItem ->
                    selectedBottomRoute == navItem.route
                },
                onItemClick = { navItem ->
                    if (selectedBottomRoute != navItem.route) {
                        selectedBottomRoute = navItem.route

                        navController.navigate(navItem.route) {
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