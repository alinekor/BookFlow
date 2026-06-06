package com.example.bookflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookflow.R

enum class AppBottomDestination(val icon: ImageVector, val labelRes: Int) {
    Search(
        icon = Icons.Outlined.Search,
        labelRes = R.string.bottom_nav_destination_search,
    ),
    BookDetails(
        icon = Icons.Outlined.Description,
        labelRes = R.string.bottom_nav_destination_book_details,
    ),
    MyShelf(
        icon = Icons.Outlined.CollectionsBookmark,
        labelRes = R.string.bottom_nav_destination_my_shelf,
    ),
}

sealed class AppDestination {
    data object BookDetails : AppDestination()
}