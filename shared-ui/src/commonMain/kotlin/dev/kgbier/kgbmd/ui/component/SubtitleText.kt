package dev.kgbier.kgbmd.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SubtitleText(
    title: String,
    modifier: Modifier = Modifier,
) = Text(
    title,
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = modifier
)

@Preview
@Composable
fun SubtitleTextPreview() = MaterialTheme {
    SubtitleText("Title")
}
