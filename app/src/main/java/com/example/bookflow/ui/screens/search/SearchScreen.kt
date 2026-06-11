package com.example.bookflow.ui.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookflow.R
import com.example.bookflow.data.model.Book
import com.example.bookflow.presentation.search.SearchEvent
import com.example.bookflow.presentation.search.SearchResultState
import com.example.bookflow.presentation.search.SearchViewModel
import com.example.bookflow.presentation.search.getInitBooks
import com.example.bookflow.ui.components.AppSearchBar
import com.example.bookflow.ui.components.BookListItem
import com.example.bookflow.ui.components.StatusMessage
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun SearchScreen(
    onNavAction: (action: SearchNavAction) -> Unit,
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
    )
}

@Composable
private fun SearchScreen(
    query: String,
    isSearchExpanded: Boolean,
    searchState: SearchResultState,
    onSearchEvent: (event: SearchEvent) -> Unit,
    onNavAction: (action: SearchNavAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(Modifier.fillMaxSize()) {
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
            modifier = Modifier.align(Alignment.CenterHorizontally)
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

                is SearchResultState.Error -> SearchErrorState(
                    onRetryClick = {
                        onSearchEvent(SearchEvent.OnRetrySearch)
                    }
                )
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
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(3F)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusMessage(
                image = {
                    Image(
                        imageVector = Icons.Outlined.Search,
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
        Spacer(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SearchProgressState() {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(3F)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
        Spacer(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SearchContentState(
    books: List<Book>,
    onBookClick: (key: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(modifier = Modifier) {
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

@Composable
private fun SearchErrorState(
    onRetryClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(3F)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusMessage(
                image = {
                    Image(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                },
                title = stringResource(R.string.search_error_state_title),
                description = stringResource(R.string.search_error_state_description),
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onRetryClick) {
                Text(
                    text = stringResource(R.string.search_error_retry_button),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
        Spacer(
            modifier = Modifier
                .weight(2F)
                .fillMaxWidth()
        )
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

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchErrorStatePreview() {
    BookFlowTheme {
        Surface {
            SearchScreen(
                query = "the",
                isSearchExpanded = true,
                searchState = SearchResultState.Error,
                onSearchEvent = {},
                onNavAction = {},
            )
        }
    }
}