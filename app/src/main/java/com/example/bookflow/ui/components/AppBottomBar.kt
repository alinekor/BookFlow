package com.example.bookflow.ui.components

import android.content.res.Configuration
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookflow.ui.extensions.getAvailableBottomBarItems
import com.example.bookflow.ui.navigation.GraphRoute
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun AppBottomBar(
    items: List<NavBarItem>,
    isItemSelected: (NavBarItem) -> Boolean,
    onItemClick: (NavBarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            AppNavBarItem(
                item = item,
                isSelected = isItemSelected(item),
                onItemClick = { onItemClick(item) },
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppBottomBarPreview() {
    BookFlowTheme {
        AppBottomBar(
            items = getAvailableBottomBarItems(),
            isItemSelected = { it.graphRoute == GraphRoute.SearchGraph },
            onItemClick = {},
        )
    }
}