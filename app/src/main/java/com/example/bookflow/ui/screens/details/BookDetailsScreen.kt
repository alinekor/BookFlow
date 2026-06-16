package com.example.bookflow.ui.screens.details

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookflow.R
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.presentation.details.BookDetailsEvent
import com.example.bookflow.presentation.details.BookDetailsScreenState
import com.example.bookflow.presentation.details.BookDetailsViewModel
import com.example.bookflow.presentation.search.getInitBookDetails
import com.example.bookflow.ui.components.nav_bar.BackTopAppBar
import com.example.bookflow.ui.components.tags.OutlinedTag
import com.example.bookflow.ui.components.tags.TagFlowRow
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookDetailsScreen(
    onNavAction: (action: BookDetailsNavAction) -> Unit,
    viewModel: BookDetailsViewModel = viewModel(
        factory = BookDetailsViewModel.Factory
    ),
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    BookDetailsScreen(
        screenState = screenState,
        onScreenEvent = viewModel::onScreenEvent,
        onNavAction = onNavAction,
    )
}

@Composable
private fun BookDetailsScreen(
    screenState: BookDetailsScreenState,
    onScreenEvent: (event: BookDetailsEvent) -> Unit,
    onNavAction: (action: BookDetailsNavAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            BackTopAppBar(
                onBackClick = {
                    onNavAction(BookDetailsNavAction.NavigateBack)
                },
                highlighted = isScrolled,
            )
        },
        floatingActionButton = {
            if (screenState is BookDetailsScreenState.Content) {
                FloatingActionButton(
                    onClick = { onScreenEvent(BookDetailsEvent.OnSaveToLibraryClick) }
                ) {
                    val icon = if (screenState.savedToLibrary) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        when (screenState) {
            BookDetailsScreenState.Initial -> {}

            BookDetailsScreenState.Loading -> {} //TODO

            BookDetailsScreenState.Error -> {} //TODO

            is BookDetailsScreenState.Content -> BookContent(
                innerPadding = innerPadding,
                bookDetails = screenState.bookDetails,
                scrollState = scrollState,
            )
        }
    }
}

@Composable
private fun BookContent(
    innerPadding: PaddingValues,
    bookDetails: BookDetails,
    scrollState: ScrollState,
) {
    val authors = remember(bookDetails.authors) {
        formatAuthors(bookDetails.authors)
    }
    val tags = bookDetails.subjects.takeIf { it.isNotEmpty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
            .padding(bottom = 108.dp, start = 12.dp, end = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bookDetails.cover?.largeUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.book_placeholder),
            error = painterResource(R.drawable.book_placeholder),
            modifier = Modifier
                .size(width = 160.dp, height = 240.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = bookDetails.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        authors?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
            )
        }
        bookDetails.description?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        bookDetails.publishYear?.let { year ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    R.string.book_details_publication_year_format,
                    year.toString()
                ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
            )
        }
        tags?.let {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.book_details_tags_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TagFlowRow(
                tags = it,
                tagContent = { tagName ->
                    OutlinedTag(tagName = tagName)
                }
            )
        }
    }
}

private fun formatAuthors(authors: List<String>): String? {
    return authors.takeIf { it.isNotEmpty() }?.joinToString(", ")
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Night Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookDetailsContentStatePreview() {
    BookFlowTheme {
        BookDetailsScreen(
            screenState = BookDetailsScreenState.Content(
                bookDetails = getInitBookDetails(),
                savedToLibrary = true,
            ),
            onScreenEvent = {},
            onNavAction = {},
        )
    }
}