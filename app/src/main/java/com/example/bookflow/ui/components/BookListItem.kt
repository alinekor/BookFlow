package com.example.bookflow.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookflow.R
import com.example.bookflow.data.model.Book
import com.example.bookflow.data.model.BookCover
import com.example.bookflow.ui.theme.BookFlowTheme

@Composable
fun BookListItem(
    item: Book,
    onBookClick: (key: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val authors = remember(item.authors) {
        formatAuthors(item.authors)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onClick = { onBookClick(item.key) },
    ) {
        Row(
            modifier = modifier.padding(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.book_cover_test_medium),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 56.dp, height = 84.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1F),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                )
                authors?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                item.publishYear?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

private fun formatAuthors(authors: List<String>): String? {
    return authors.takeIf { it.isNotEmpty() }?.joinToString(",")
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookListItemPreview() {
    BookFlowTheme {
        BookListItem(
            item = Book(
                key = "/works/OL27448W",
                title = "The Lord of the Rings",
                authors = listOf("J. R. R. Tolkien"),
                publishYear = 1954,
                cover = BookCover(id = 8231856),
            ),
            onBookClick = {},
        )
    }
}