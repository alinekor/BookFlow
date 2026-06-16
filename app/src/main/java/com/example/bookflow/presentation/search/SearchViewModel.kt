package com.example.bookflow.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.ui.utils.imitateLoading
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenState.INITIAL)
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    private val initBooks: List<Book> = getInitBooks()

    init {
        viewModelScope.launch {
            screenState
                .map { it.query }
                .debounce(QUERY_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collectLatest { query -> search(query) }
        }
    }

    private suspend fun search(query: String) {
        if (query.isBlank()) {
            updateSearchState(newState = SearchState.Initial)
            return
        }

        updateSearchState(newState = SearchState.Loading)
        imitateLoading()

        try {
            val filteredBooks = initBooks.filter {
                it.title.contains(query.trim(), ignoreCase = true)
            }

            updateSearchState(
                newState = if (filteredBooks.isEmpty()) {
                    SearchState.EmptySearch
                } else {
                    SearchState.Content(filteredBooks)
                }
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            updateSearchState(newState = SearchState.Error)
        }
    }

    fun onScreenEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> onQueryChanged(event.newValue)
            is SearchEvent.OnExpandedChange -> onExpandedChange(event.newValue)
            SearchEvent.OnSearchClick -> onSearch()
            SearchEvent.OnClearQueryClick -> onClearQueryClick()
            SearchEvent.OnRetrySearch -> onRetrySearchClick()
        }
    }

    private fun onQueryChanged(newValue: String) {
        updateQuery(newValue = newValue)
    }

    private fun onExpandedChange(newValue: Boolean) {
        updateIsSearchExpanded(newValue = newValue)
    }

    private fun onSearch() {
        if (screenState.value.query.isNotEmpty()) return
        updateIsSearchExpanded(newValue = false)
    }

    private fun onClearQueryClick() {
        if (screenState.value.query.isNotEmpty()) {
            updateQuery(newValue = "")
        } else {
            updateIsSearchExpanded(newValue = false)
        }
    }

    private fun onRetrySearchClick() {
        viewModelScope.launch {
            search(screenState.value.query)
        }
    }

    private fun updateSearchState(newState: SearchState) {
        _screenState.update { it.copy(searchState = newState) }
    }

    private fun updateQuery(newValue: String) {
        _screenState.update { it.copy(query = newValue) }
    }

    private fun updateIsSearchExpanded(newValue: Boolean) {
        _screenState.update { it.copy(isSearchExpanded = newValue) }
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