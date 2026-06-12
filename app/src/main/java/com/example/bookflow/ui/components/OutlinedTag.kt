package com.example.bookflow.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun OutlinedTag(
    tagName: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier,
    ) {
        Text(
            text = tagName,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
        )
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Night Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OutlinedTagPreview() {
    BookFlowTheme {
        Surface {
            OutlinedTag(
                tagName = "Fantasy"
            )
        }
    }
}