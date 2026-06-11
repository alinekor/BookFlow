package com.example.bookflow.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _isSearchExpanded = MutableStateFlow(false)
    val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()

    private val _searchState = MutableStateFlow<SearchResultState>(SearchResultState.Initial)
    val searchState: StateFlow<SearchResultState> = _searchState.asStateFlow()

    private val initBooks: List<Book> = getInitBooks()

    init {
        viewModelScope.launch {
            _query
                .debounce(QUERY_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collectLatest { query -> search(query) }
        }
    }

    private suspend fun search(query: String) {
        if (query.isBlank()) {
            _searchState.value = SearchResultState.Initial
            return
        }

        _searchState.value = SearchResultState.Loading
        delay(2000L) // for test

        try {
            val filteredBooks = initBooks.filter {
                it.title.contains(query.trim(), ignoreCase = true)
            }

            _searchState.value = if (filteredBooks.isEmpty()) {
                SearchResultState.EmptySearch
            } else {
                SearchResultState.Content(filteredBooks)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _searchState.value = SearchResultState.Error
        }
    }

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
        viewModelScope.launch {
            search(_query.value)
        }
    }

    companion object {
        private const val QUERY_DEBOUNCE_MS = 300L
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