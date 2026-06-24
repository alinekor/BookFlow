package com.example.bookflow.presentation.search

import androidx.lifecycle.viewModelScope
import com.example.bookflow.data.model.Book
import com.example.bookflow.presentation.base.BaseViewModel
import com.example.bookflow.ui.screens.search.getInitBooks
import com.example.bookflow.ui.utils.imitateLoading
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class)
class SearchViewModel : BaseViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenState.INITIAL)
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    private val initBooks: List<Book> = getInitBooks()

    init {
        observeErrors()
        observeQuery()
    }

    private fun observeErrors() {
        viewModelScope.launch {
            error.collect {
                updateSearchState(newState = SearchState.Error)
            }
        }
    }

    private fun observeQuery() {
        viewModelScope.launch {
            screenState
                .map { it.query }
                .debounce(QUERY_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collectLatest { query ->
                    catchError { search(query) }
                }
        }
    }

    private suspend fun search(query: String) {
        if (query.isBlank()) {
            updateSearchState(newState = SearchState.Initial)
            return
        }

        updateSearchState(newState = SearchState.Loading)
        imitateLoading()

        val filteredBooks = withContext(Dispatchers.Default) {
            initBooks.filter {
                it.title.contains(query.trim(), ignoreCase = true)
            }
        }

        updateSearchState(
            newState = if (filteredBooks.isEmpty()) {
                SearchState.EmptySearch
            } else {
                SearchState.Content(filteredBooks)
            }
        )
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
        launchCatching {
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