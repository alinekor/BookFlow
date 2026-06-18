package com.example.bookflow.presentation.search

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.repository.BookRepository
import com.example.bookflow.presentation.base.BaseViewModel
import com.example.bookflow.ui.extensions.isValidSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: BookRepository,
) : BaseViewModel() {

    private val query = MutableStateFlow(SearchScreenState.INITIAL.query)
    private val isSearchExpanded = MutableStateFlow(SearchScreenState.INITIAL.isSearchExpanded)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val booksPagingData: Flow<PagingData<Book>> =
        query
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

    val screenState: StateFlow<SearchScreenState> =
        combine(query, isSearchExpanded) { query, isSearchExpanded ->
            SearchScreenState(
                query = query,
                isSearchExpanded = isSearchExpanded,
                booksPagingData = booksPagingData,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
            initialValue = SearchScreenState.INITIAL.copy(
                booksPagingData = booksPagingData,
            )
        )

    fun onSearchEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> onQueryChanged(event.newValue)
            is SearchEvent.OnExpandedChange -> onExpandedChange(event.newValue)
            SearchEvent.OnSearchClick -> onSearch()
            SearchEvent.OnClearQueryClick -> onClearQueryClick()
        }
    }

    private fun onQueryChanged(newValue: String) {
        query.value = newValue
    }

    private fun onExpandedChange(newValue: Boolean) {
        isSearchExpanded.value = newValue
    }

    private fun onSearch() {
        if (screenState.value.query.isNotEmpty()) return
        isSearchExpanded.value = false
    }

    private fun onClearQueryClick() {
        if (screenState.value.query.isNotEmpty()) {
            query.value = ""
        } else {
            isSearchExpanded.value = false
        }
    }

    companion object {
        private const val QUERY_DEBOUNCE_MS = 500L
        private const val SUBSCRIPTION_TIMEOUT_MS = 5000L

        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                )
                SearchViewModel(
                    repository = BookRepository(application.applicationContext)
                )
            }
        }
    }
}