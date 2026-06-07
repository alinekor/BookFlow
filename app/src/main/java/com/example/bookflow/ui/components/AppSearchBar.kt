package com.example.bookflow.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookflow.R
import com.example.bookflow.ui.theme.BookFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    searchPlaceholderRes: Int = R.string.search_hint_default,
    leadingIcon: ImageVector? = Icons.Default.Search,
    trailingIcon: ImageVector? = Icons.Default.Close,
    onTrailingIconClick: (() -> Unit) = {},
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = { Text(stringResource(searchPlaceholderRes)) },
                leadingIcon = {
                    leadingIcon ?: return@InputField

                    Icon(leadingIcon, contentDescription = null)
                },
                trailingIcon = {
                    trailingIcon ?: return@InputField

                    IconButton(onClick = onTrailingIconClick) {
                        Icon(trailingIcon, contentDescription = null)
                    }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        windowInsets = SearchBarDefaults.windowInsets,
    ) {
        //TODO: INIT, EMPTY, PROGRESS STATES
        searchContent()
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppSearchBarDefaultPreview() {
    BookFlowTheme {
        AppSearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            expanded = false,
            onExpandedChange = {},
            trailingIcon = null,
            searchContent = {},
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppSearchBarExpandedPreview() {
    BookFlowTheme {
        AppSearchBar(
            query = "Ams",
            onQueryChange = {},
            onSearch = {},
            expanded = true,
            onExpandedChange = {},
            searchContent = {
                ListItem(headlineContent = { Text("Amsterdam") })
                ListItem(headlineContent = { Text("Rotterdam") })
                ListItem(headlineContent = { Text("Utrecht") })
            },
        )
    }
}