package com.example.bookflow.ui.components.paging

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookflow.R
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun PagingErrorItem(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
        )
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.retry_button))
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PagingErrorItemPreview() {
    BookFlowTheme {
        Surface {
            PagingErrorItem(
                errorMessage = "Page loading error",
                onRetry = {},
            )
        }
    }
}