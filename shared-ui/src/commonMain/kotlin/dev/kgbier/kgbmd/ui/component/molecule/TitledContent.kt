package dev.kgbier.kgbmd.ui.component.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.kgbier.kgbmd.ui.component.atom.SubtitleText


@Composable
fun TitledContent(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
) {
    SubtitleText(title,)
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