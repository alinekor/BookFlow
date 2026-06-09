package com.example.bookflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookflow.R
import kotlinx.serialization.Serializable

enum class AppBottomDestination(val icon: ImageVector, val labelRes: Int) {
    Search(
        icon = Icons.Outlined.Search,
        labelRes = R.string.bottom_nav_destination_search,
    ),
    MyShelf(
        icon = Icons.Outlined.CollectionsBookmark,
        labelRes = R.string.bottom_nav_destination_my_shelf,
    ),
}

sealed class AppDestination {
    @Serializable
    data class BookDetails(val bookKey: String) : AppDestination()
}