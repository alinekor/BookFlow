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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.bookflow.R
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.presentation.search.SearchEvent
import com.example.bookflow.presentation.search.SearchScreenState
import com.example.bookflow.presentation.search.SearchViewModel
import com.example.bookflow.ui.components.books.BookListItem
import com.example.bookflow.ui.components.paging.PagingLoadStateView
import com.example.bookflow.ui.components.placehoders.StatusMessage
import com.example.bookflow.ui.components.search.AppSearchBar
import com.example.bookflow.ui.theme.BookFlowTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchScreen(
    onNavAction: (action: SearchNavAction) -> Unit,
    viewModel: SearchViewModel = viewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val books = viewModel.books.collectAsLazyPagingItems()

    SearchScreen(
        screenState = screenState,
        books = books,
        onSearchEvent = viewModel::onSearchEvent,
        onNavAction = onNavAction,
    )
}

@Composable
private fun SearchScreen(
    screenState: SearchScreenState,
    books: LazyPagingItems<Book>,
    onSearchEvent: (event: SearchEvent) -> Unit,
    onNavAction: (action: SearchNavAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val isSearchExpanded = screenState.isSearchExpanded
    val booksRefreshLoadState = books.loadState.refresh

    Column(Modifier.fillMaxSize()) {
        AppSearchBar(
            query = screenState.query,
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
            when {
                screenState.query.isBlank() -> SearchEmptyState(
                    titleRes = R.string.search_initial_state_title,
                    descriptionRes = R.string.search_initial_state_description,
                )

                booksRefreshLoadState is LoadState.Loading -> SearchProgressState()

                booksRefreshLoadState is LoadState.Error -> SearchErrorState(
                    onRetryClick = { books::retry }
                )

                booksRefreshLoadState is LoadState.NotLoading && books.itemCount == 0 -> SearchEmptyState(
                    titleRes = R.string.search_empty_state_title,
                    descriptionRes = R.string.search_empty_state_description,
                )

                booksRefreshLoadState is LoadState.NotLoading -> {
                    BooksList(
                        books = books,
                        onBookClick = {
                            onNavAction(SearchNavAction.OpenBookDetails(bookKey = it))
                        },
                        onRetryClick = { books::retry },
                    )
                }
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
fun BooksList(
    books: LazyPagingItems<Book>,
    onBookClick: (key: String) -> Unit,
    onRetryClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    LazyColumn {
        items(
            count = books.itemCount,
            key = { index -> books[index]?.key ?: "book_$index" }
        ) { index ->
            books[index]?.let { book ->
                BookListItem(
                    item = book,
                    onBookClick = {
                        focusManager.clearFocus()
                        onBookClick(it)
                    },
                )
            }
        }
        item {
            PagingLoadStateView(
                loadState = books.loadState.append,
                onRetry = onRetryClick,
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
                    text = stringResource(R.string.retry_button),
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
fun SearchEmptyStatePreview() {
    BookFlowTheme {
        Surface {
            SearchEmptyState(
                titleRes = R.string.search_empty_state_title,
                descriptionRes = R.string.search_empty_state_description,
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
            SearchProgressState()
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BooksListPreview() {
    val books = flowOf(
        PagingData.from(getInitBooks())
    ).collectAsLazyPagingItems()

    BookFlowTheme {
        Surface {
            BooksList(
                books = books,
                onBookClick = {},
                onRetryClick = {},
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
            SearchErrorState(
                onRetryClick = {},
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    val books = flowOf(
        PagingData.from(emptyList<Book>())
    ).collectAsLazyPagingItems()

    BookFlowTheme {
        Surface {
            SearchScreen(
                screenState = SearchScreenState(
                    query = "",
                    isSearchExpanded = true,
                ),
                books = books,
                onSearchEvent = {},
                onNavAction = {},
            )
        }
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