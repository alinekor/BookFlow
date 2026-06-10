package com.example.bookflow.ui.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookflow.R
import com.example.bookflow.data.model.Book
import com.example.bookflow.presentation.details.SearchEvent
import com.example.bookflow.presentation.details.SearchResultState
import com.example.bookflow.presentation.details.SearchViewModel
import com.example.bookflow.presentation.details.getInitBooks
import com.example.bookflow.ui.components.AppSearchBar
import com.example.bookflow.ui.components.BookListItem
import com.example.bookflow.ui.components.EmptyPlaceholder
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

    Column(
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
                .align(Alignment.CenterHorizontally)
                .semantics { traversalIndex = 0f },
        ) {
            when (searchState) {
                SearchResultState.Initial -> SearchEmptyState(
                    titleRes = R.string.search_initial_state_title,
                    descriptionRes = R.string.search_initial_state_description,
                )

                SearchResultState.Loading -> SearchProgressState()

                SearchResultState.EmptySearch -> SearchEmptyState(
                    titleRes = R.string.search_empty_state_title,
                    descriptionRes = R.string.search_empty_state_description,
                )

                is SearchResultState.Content -> SearchContentState(
                    books = searchState.books,
                    onBookClick = {
                        onNavAction(SearchNavAction.OpenBookDetails(bookKey = it))
                    }
                )

                is SearchResultState.Error -> {} //TODO()
            }
        }

        if (!isSearchExpanded) {
            SearchEmptyState(
                titleRes = R.string.search_initial_state_title,
                descriptionRes = R.string.search_initial_state_description,
            )
        }
    }
}

@Composable
private fun SearchEmptyState(titleRes: Int, descriptionRes: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        contentAlignment = Alignment.Center,
    ) {
        EmptyPlaceholder(
            image = {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.primary,
                    )
                )
            },
            title = stringResource(titleRes),
            description = stringResource(descriptionRes),
        )
    }
}

@Composable
private fun SearchProgressState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchContentState(
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
fun SearchProgressStatePreview() {
    BookFlowTheme {
        Surface {
            SearchScreen(
                query = "the",
                isSearchExpanded = true,
                searchState = SearchResultState.Loading,
                onSearchEvent = {},
                onNavAction = {},
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchEmptyStatePreview() {
    BookFlowTheme {
        Surface {
            SearchScreen(
                query = "th",
                isSearchExpanded = true,
                searchState = SearchResultState.EmptySearch,
                onSearchEvent = {},
                onNavAction = {},
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchContentStatePreview() {
    BookFlowTheme {
        Surface {
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
}