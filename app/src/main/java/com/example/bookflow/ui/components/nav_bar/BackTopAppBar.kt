package com.example.bookflow.ui.components.nav_bar

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookflow.ui.theme.BookFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onBackClick: () -> Unit,
    highlighted: Boolean,
) {
    val containerColor by animateColorAsState(
        targetValue = if (highlighted) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    )

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = backIcon, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor
        ),
        modifier = modifier,
    )
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Night Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BackTopAppBarPreview() {
    BookFlowTheme {
        BackTopAppBar(
            title = "Заголовок",
            onBackClick = {},
            highlighted = false,
        )
    }
}