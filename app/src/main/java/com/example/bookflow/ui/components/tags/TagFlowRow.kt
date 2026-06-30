package com.example.bookflow.ui.components.tags

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun TagFlowRow(
    tags: List<String>,
    tagContent: @Composable (tagName: String) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
) {
    FlowRow(
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        modifier = modifier,
    ) {
        tags.forEach { tagName -> tagContent(tagName) }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Night Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagFlowRowPreview() {
    BookFlowTheme {
        Surface {
            TagFlowRow(
                tags = listOf(
                    "Fantasy",
                    "Science Fiction",
                    "Thriller",
                    "Romance",
                    "Adventure",
                    "Classics",
                ),
                tagContent = { tagName ->
                    OutlinedTag(tagName = tagName)
                }
            )
        }
    }
}