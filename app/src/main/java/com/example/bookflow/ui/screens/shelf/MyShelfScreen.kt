package com.example.bookflow.ui.screens.shelf

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookflow.R
import com.example.bookflow.data.model.Book
import com.example.bookflow.presentation.shelf.MyShelfEvent
import com.example.bookflow.presentation.shelf.MyShelfScreenState
import com.example.bookflow.presentation.shelf.MyShelfViewModel
import com.example.bookflow.ui.components.books.BookListItem
import com.example.bookflow.ui.components.nav_bar.TitleTopAppBar
import com.example.bookflow.ui.components.placehoders.StatusMessage
import com.example.bookflow.ui.screens.search.getInitBooks
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun MyShelfScreen(
    router: IMyShelfRouter,
    viewModel: MyShelfViewModel = viewModel(
        factory = MyShelfViewModel.Factory,
    )
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    MyShelfScreen(
        screenState = screenState,
        onScreenEvent = viewModel::onScreenEvent,
        router = router,
    )
}

@Composable
fun MyShelfScreen(
    screenState: MyShelfScreenState,
    onScreenEvent: (event: MyShelfEvent) -> Unit,
    router: IMyShelfRouter,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.my_shelf_title),
                highlighted = false,
            )
        },
    ) { innerPadding ->
        when (screenState) {
            MyShelfScreenState.Initial -> {}

            MyShelfScreenState.Loading -> MyShelfLoadingState(innerPadding)

            MyShelfScreenState.Empty -> InfoWithActionState(
                innerPadding = innerPadding,
                image = Icons.Outlined.Book,
                title = stringResource(R.string.my_shelf_empty_state_title),
                description = stringResource(R.string.my_shelf_empty_state_description),
                actionButtonText = stringResource(R.string.my_shelf_empty_state_action),
                onActionButtonClick = { router.openSearch() }
            )

            is MyShelfScreenState.Content -> MyShelfContentState(
                innerPadding = innerPadding,
                books = screenState.books,
                onBookClick = { router.openBookDetails(it) }
            )

            MyShelfScreenState.Error -> InfoWithActionState(
                innerPadding = innerPadding,
                image = Icons.Outlined.ErrorOutline,
                title = stringResource(R.string.my_shelf_error_state_title),
                description = stringResource(R.string.my_shelf_error_state_description),
                actionButtonText = stringResource(R.string.retry_button),
                onActionButtonClick = { onScreenEvent(MyShelfEvent.OnRetryClick) }
            )
        }
    }
}

@Composable
private fun MyShelfLoadingState(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(bottom = 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun InfoWithActionState(
    innerPadding: PaddingValues,
    image: ImageVector,
    title: String,
    description: String,
    actionButtonText: String,
    onActionButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(bottom = 56.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatusMessage(
            image = {
                Image(
                    imageVector = image,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.primary,
                    )
                )
            },
            title = title,
            description = description,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onActionButtonClick) {
            Text(
                text = actionButtonText,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun MyShelfContentState(
    innerPadding: PaddingValues,
    books: List<Book>,
    onBookClick: (bookKey: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        items(books, key = { it.key }) { book ->
            BookListItem(
                item = book,
                onBookClick = { onBookClick(it) },
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyShelfLoadingStatePreview() {
    BookFlowTheme {
        MyShelfScreen(
            screenState = MyShelfScreenState.Loading,
            onScreenEvent = {},
            router = PreviewMyShelfRouter,
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyShelfEmptyStatePreview() {
    BookFlowTheme {
        MyShelfScreen(
            screenState = MyShelfScreenState.Empty,
            onScreenEvent = {},
            router = PreviewMyShelfRouter,
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyShelfErrorStatePreview() {
    BookFlowTheme {
        MyShelfScreen(
            screenState = MyShelfScreenState.Error,
            onScreenEvent = {},
            router = PreviewMyShelfRouter,
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyShelfContentStatePreview() {
    BookFlowTheme {
        MyShelfScreen(
            screenState = MyShelfScreenState.Content(
                books = getInitBooks(),
            ),
            onScreenEvent = {},
            router = PreviewMyShelfRouter,
        )
    }
}