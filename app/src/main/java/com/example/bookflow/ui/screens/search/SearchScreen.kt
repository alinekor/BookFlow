package com.example.bookflow.ui.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookflow.data.model.Book
import com.example.bookflow.presentation.details.SearchEvent
import com.example.bookflow.presentation.details.SearchResultState
import com.example.bookflow.presentation.details.SearchViewModel
import com.example.bookflow.presentation.details.getInitBooks
import com.example.bookflow.ui.components.AppSearchBar
import com.example.bookflow.ui.components.BookListItem
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun SearchScreen(
    onNavAction: (action: SearchNavAction) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val isSearchExpanded by viewModel.isSearchExpanded.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    SearchScreen(
        query = query,
        isSearchExpanded = isSearchExpanded,
        searchState = searchState,
        onSearchEvent = viewModel::onSearchEvent,
        onNavAction = onNavAction,
        modifier = modifier,
    )
}

@Composable
private fun SearchScreen(
    query: String,
    isSearchExpanded: Boolean,
    searchState: SearchResultState,
    onSearchEvent: (event: SearchEvent) -> Unit,
    onNavAction: (action: SearchNavAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        AppSearchBar(
            query = query,
            onQueryChange = {
                onSearchEvent(SearchEvent.OnQueryChange(it))
            },
            onSearch = {
                focusManager.clearFocus()
                onSearchEvent(SearchEvent.OnSearchClick)
            },
            expanded = isSearchExpanded,
            onExpandedChange = {
                onSearchEvent(SearchEvent.OnExpandedChange(it))
            },
            trailingIcon = if (isSearchExpanded) Icons.Default.Close else null,
            onTrailingIconClick = {
                onSearchEvent(SearchEvent.OnClearQueryClick)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
        ) {
            when (searchState) {
                SearchResultState.Initial -> {} //TODO("epxanded = true")

                SearchResultState.Loading -> {} //TODO()

                SearchResultState.EmptySearch -> {} //TODO()

                is SearchResultState.Content -> SearchContent(
                    books = searchState.books,
                    onBookClick = {
                        onNavAction(SearchNavAction.OpenBookDetails(bookKey = it))
                    }
                )

                is SearchResultState.Error -> {} //TODO()
            }
        }

        //TODO: Initial state and expanded = false
    }
}

@Composable
private fun SearchContent(
    books: List<Book>,
    onBookClick: (key: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(modifier = modifier) {
        items(books, key = { it.key }) { item ->
            BookListItem(
                item = item,
                onBookClick = {
                    focusManager.clearFocus()
                    onBookClick(it)
                },
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    BookFlowTheme {
        SearchScreen(
            query = "the",
            isSearchExpanded = true,
            searchState = SearchResultState.Content(
                books = getInitBooks()
            ),
            onSearchEvent = {},
            onNavAction = {},
        )
    }
}