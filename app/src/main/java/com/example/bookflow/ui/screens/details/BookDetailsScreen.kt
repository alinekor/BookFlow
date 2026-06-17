package com.example.bookflow.ui.screens.details

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookflow.R
import com.example.bookflow.data.model.BookDetails
import com.example.bookflow.ui.components.nav_bar.BackTopAppBar
import com.example.bookflow.ui.components.tags.OutlinedTag
import com.example.bookflow.ui.components.tags.TagFlowRow
import com.example.bookflow.ui.screens.search.getInitBookDetails
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookDetailsScreen(
    bookKey: String,
    onBackClick: () -> Unit,
) {
    val bookDetails = remember(bookKey) {
        val stubBookDetails = getInitBookDetails()
        stubBookDetails.copy(
            key = bookKey,
            title = "${stubBookDetails.title} - $bookKey"
        )
    }
    var savedToLibrary by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            BackTopAppBar(
                onBackClick = onBackClick,
                highlighted = isScrolled,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { savedToLibrary = !savedToLibrary }
            ) {
                Icon(
                    imageVector = if (savedToLibrary) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        BookContent(innerPadding, bookDetails, scrollState)
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
        Image(
            painter = painterResource(R.drawable.book_cover_test_large),
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
fun BookDetailsScreenPreview() {
    BookFlowTheme {
        BookDetailsScreen(
            bookKey = "123",
            onBackClick = {},
        )
    }
}