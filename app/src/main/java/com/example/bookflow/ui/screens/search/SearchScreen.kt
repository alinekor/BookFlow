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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.ui.components.AppSearchBar
import com.example.bookflow.ui.components.BookListItem
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBookClick: (key: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val initBooks = getInitBooks()
    val filteredBooks = initBooks.filter {
        query.isNotEmpty() && it.title.contains(query, ignoreCase = true)
    }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        AppSearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                focusManager.clearFocus()
                if (query.isEmpty()) expanded = false
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            trailingIcon = if (expanded) Icons.Default.Close else null,
            onTrailingIconClick = {
                if (query.isNotEmpty()) {
                    query = ""
                } else {
                    focusManager.clearFocus()
                    expanded = false
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
        ) {
            LazyColumn {
                items(filteredBooks, key = { it.key }) { item ->
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
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    BookFlowTheme {
        SearchScreen(
            onBookClick = {},
        )
    }
}

private fun getInitBooks(): List<Book> {
    return List(10) { i ->
        Book(
            key = "/works/OL27448W_$i",
            title = "The Lord of the Rings $i",
            authors = listOf("J. R. R. Tolkien"),
            publishYear = 1954,
            cover = BookCover(id = 8231856),
        )
    }
}