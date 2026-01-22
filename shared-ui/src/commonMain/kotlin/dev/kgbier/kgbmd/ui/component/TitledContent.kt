package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun TitledContent(
    title: String,
    body: String,
    modifier: Modifier = Modifier.Companion,
) = Column(
    modifier = modifier
) {
    SubtitleText(title)
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Preview
@Composable
fun TitledContentPreview() = MaterialTheme {
    TitledContent(
        title = "Title",
        body = "Body",
    )
}