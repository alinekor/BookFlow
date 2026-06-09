package com.example.bookflow.presentation.details

sealed class SearchEvent {

    data class OnQueryChange(val newValue: String) : SearchEvent()

    data class OnExpandedChange(val newValue: Boolean) : SearchEvent()

    data object OnSearchClick : SearchEvent()

    data object OnClearQueryClick : SearchEvent()
}