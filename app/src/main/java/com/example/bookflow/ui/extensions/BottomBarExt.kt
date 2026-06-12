package com.example.bookflow.ui.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Search
import com.example.bookflow.R
import com.example.bookflow.ui.components.NavBarItem
import com.example.bookflow.ui.navigation.GraphRoute

fun getAvailableBottomBarItems(): List<NavBarItem> {
    return listOf(
        GraphRoute.SearchGraph,
        GraphRoute.MyShelfGraph,
    ).toBottomBarItems()
}

fun List<GraphRoute>.toBottomBarItems() = this.map { route ->
    val (icon, labelRes) = when (route) {
        GraphRoute.SearchGraph ->
            Icons.Outlined.Search to R.string.bottom_nav_destination_search

        GraphRoute.MyShelfGraph ->
            Icons.Outlined.CollectionsBookmark to R.string.bottom_nav_destination_my_shelf
    }

    NavBarItem(
        graphRoute = route,
        icon = icon,
        labelRes = labelRes,
    )
}