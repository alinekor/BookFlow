package com.example.bookflow.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.ui.extensions.isValidSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val repository = BookRepository()

    private val _screenState = MutableStateFlow(SearchScreenState.INITIAL)
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: Flow<PagingData<Book>> =
        screenState
            .debounce(QUERY_DEBOUNCE_MS)
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isValidSearchQuery()) {
                    repository.searchBooks(query = query)
                } else {
                    flowOf(PagingData.empty())
                }
            }
            .cachedIn(viewModelScope)

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> onQueryChanged(event.newValue)
            is SearchEvent.OnExpandedChange -> onExpandedChange(event.newValue)
            SearchEvent.OnSearchClick -> onSearch()
            SearchEvent.OnClearQueryClick -> onClearQueryClick()
        }
    }

    private fun onQueryChanged(newValue: String) {
        _screenState.update {
            it.copy(query = newValue)
        }
    }

    private fun onExpandedChange(newValue: Boolean) {
        _screenState.update {
            it.copy(isSearchExpanded = newValue)
        }
    }

    private fun onSearch() {
        if (screenState.value.query.isNotEmpty()) return

        _screenState.update {
            it.copy(isSearchExpanded = false)
        }
    }

    private fun onClearQueryClick() {
        if (screenState.value.query.isNotEmpty()) {
            _screenState.update {
                it.copy(query = "")
            }
        } else {
            _screenState.update {
                it.copy(isSearchExpanded = false)
            }
        }
    }

    companion object {
        private const val QUERY_DEBOUNCE_MS = 500L
    }
}