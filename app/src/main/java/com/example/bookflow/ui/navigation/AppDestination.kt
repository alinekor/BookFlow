package com.example.bookflow.ui.navigation

import kotlinx.serialization.Serializable

sealed class GraphRoute {
    @Serializable
    data object SearchGraph : GraphRoute()

    @Serializable
    data object MyShelfGraph : GraphRoute()
}

sealed class AppDestination {
    @Serializable
    data object Search : AppDestination()

    @Serializable
    data object MyShelf : AppDestination()

    @Serializable
    data object BookDetails : AppDestination()
}