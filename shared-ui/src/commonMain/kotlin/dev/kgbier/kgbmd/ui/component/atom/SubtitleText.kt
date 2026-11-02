package dev.kgbier.kgbmd.ui.component.atom

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.kgbier.kgbmd.ui.component.preview.AppPreview

@Composable
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) = Text(
    text = text,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    textAlign = textAlign,
    overflow = overflow,
    maxLines = maxLines,
    minLines = minLines,
    style = MaterialTheme.typography.labelSmall,
    modifier = modifier
)

@Composable
@AppPreview.Component
fun SubtitleTextPreview() {
    SubtitleText("Title")
}
