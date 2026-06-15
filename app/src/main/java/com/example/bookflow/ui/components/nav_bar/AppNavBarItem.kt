package com.example.bookflow.ui.components.nav_bar

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookflow.R
import com.example.bookflow.ui.navigation.GraphRoute
import com.example.bookflow.ui.theme.BookFlowTheme

data class NavBarItem(
    val graphRoute: GraphRoute,
    val icon: ImageVector,
    val labelRes: Int,
)

@Composable
fun RowScope.AppNavBarItem(
    item: NavBarItem,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        icon = { Icon(item.icon, contentDescription = null) },
        label = { Text(stringResource(item.labelRes)) },
        selected = isSelected,
        onClick = onItemClick,
        modifier = modifier,
    )
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppNavBarItemPreview() {
    BookFlowTheme {
        NavigationBar {
            AppNavBarItem(
                item = NavBarItem(
                    graphRoute = GraphRoute.SearchGraph,
                    icon = Icons.Outlined.Search,
                    labelRes = R.string.bottom_nav_destination_search
                ),
                isSelected = true,
                onItemClick = {},
            )
        }
    }
}