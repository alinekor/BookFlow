package com.example.bookflow.presentation.search

data class SearchScreenState(
    val query: String,
    val isSearchExpanded: Boolean,
) {
    companion object {
        val INITIAL = SearchScreenState(query = "", isSearchExpanded = false)
    }
}