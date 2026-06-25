package com.example.bookflow.presentation.search

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.di.AppModule
import com.example.bookflow.presentation.base.BaseViewModel
import com.example.bookflow.ui.extensions.isValidSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: BookRepository,
) : BaseViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenState.INITIAL)
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()

    init {
        observeSearch()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearch() {
        screenState
            .map { it.query.trim() }
            .debounce(QUERY_DEBOUNCE_MS)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isValidSearchQuery()) {
                    repository.searchBooks(query = query)
                } else {
                    flowOf(PagingData.empty())
                }
            }
            .cachedIn(viewModelScope)
            .onEach { pagingData ->
                _screenState.update {
                    it.copy(booksPagingData = flowOf(pagingData))
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> updateQuery(event.newValue)
            is SearchEvent.OnExpandedChange -> updateSearchExpanded(event.newValue)
            SearchEvent.OnSearchClick -> onSearch()
            SearchEvent.OnClearQueryClick -> onClearQueryClick()
        }
    }

    private fun onSearch() {
        if (screenState.value.query.isNotEmpty()) return
        updateSearchExpanded(newValue = false)
    }

    private fun onClearQueryClick() {
        if (screenState.value.query.isNotEmpty()) {
            updateQuery(newValue = "")
        } else {
            updateSearchExpanded(newValue = false)
        }
    }

    private fun updateQuery(newValue: String) {
        _screenState.update {
            it.copy(query = newValue)
        }
    }

    private fun updateSearchExpanded(newValue: Boolean) {
        _screenState.update {
            it.copy(isSearchExpanded = newValue)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                SearchViewModel(repository = AppModule.bookRepository)
            }
        }

        private const val QUERY_DEBOUNCE_MS = 500L
    }
}