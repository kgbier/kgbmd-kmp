package dev.kgbier.kgbmd.ui.component.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.kgbier.kgbmd.ui.component.atom.SubtitleText
import dev.kgbier.kgbmd.ui.component.preview.AppPreview

@Composable
fun TitledContent(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) = TitledContent(
    title = title,
    modifier = modifier
) {
    Text(
        text = body,
    )
}

@Composable
fun TitledContent(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Column(
    modifier = modifier
) {
    SubtitleText(title)
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium,
    ) {
        content()
    }
}

@AppPreview.Component
@Composable
fun TitledContentPreview() {
    TitledContent(
        title = "Title",
        body = "Body",
    )
}
