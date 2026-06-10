package com.example.bookflow.ui.extensions

import com.example.bookflow.ui.components.NavBarItem
import com.example.bookflow.ui.navigation.AppBottomDestination

fun getAvailableBottomBarItems(): List<NavBarItem> {
    return AppBottomDestination.entries.toList().toBottomBarItems()
}

fun List<AppBottomDestination>.toBottomBarItems() = this.map { destination ->
    NavBarItem(
        route = destination.name,
        icon = destination.icon,
        labelRes = destination.labelRes,
    )
}