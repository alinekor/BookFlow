package com.example.bookflow.ui.components.paging

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import com.example.bookflow.R
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun PagingLoadStateView(
    loadState: LoadState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        when (loadState) {
            is LoadState.Loading -> {
                PagingLoadingItem(modifier = modifier)
            }

            is LoadState.Error -> {
                PagingErrorItem(
                    errorMessage = loadState.error.message
                        ?: stringResource(R.string.page_loading_error_text),
                    onRetry = onRetry,
                    modifier = modifier,
                )
            }

            is LoadState.NotLoading -> {}
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PagingLoadingStatePreview() {
    BookFlowTheme {
        PagingLoadStateView(
            loadState = LoadState.Loading,
            onRetry = {}
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PagingErrorStatePreview() {
    BookFlowTheme {
        PagingLoadStateView(
            loadState = LoadState.Error(RuntimeException("Page loading error")),
            onRetry = {}
        )
    }
}