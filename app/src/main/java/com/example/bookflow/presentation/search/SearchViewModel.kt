package com.example.bookflow.presentation.search

import androidx.lifecycle.ViewModel
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _isSearchExpanded = MutableStateFlow(false)
    val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()

    private val _searchState = MutableStateFlow<SearchResultState>(SearchResultState.Initial)
    val searchState: StateFlow<SearchResultState> = _searchState.asStateFlow()

    /*val initBooks = getInitBooks()
    val filteredBooks = initBooks.filter {
        query.isNotEmpty() && it.title.contains(query, ignoreCase = true)
    }*/

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> onQueryChanged(event.newValue)
            is SearchEvent.OnExpandedChange -> onExpandedChange(event.newValue)
            SearchEvent.OnSearchClick -> onSearch()
            SearchEvent.OnClearQueryClick -> onClearQueryClick()
            SearchEvent.OnRetrySearch -> onRetrySearchClick()
        }
    }

    private fun onQueryChanged(newValue: String) {
        _query.value = newValue
    }

    private fun onExpandedChange(newValue: Boolean) {
        _isSearchExpanded.value = newValue
    }

    private fun onSearch() {
        if (query.value.isNotEmpty()) return
        _isSearchExpanded.value = false
    }

    private fun onClearQueryClick() {
        if (query.value.isNotEmpty()) {
            _query.value = ""
        } else {
            _isSearchExpanded.value = false
        }
    }

    private fun onRetrySearchClick() {
        //TODO
    }
}

fun getInitBooks(): List<Book> {
    return List(10) { i ->
        Book(
            key = "OL27448W_$i",
            title = "The Lord of the Rings $i",
            authors = listOf("J. R. R. Tolkien"),
            publishYear = 1954,
            cover = BookCover(id = 8231856),
        )
    }
}