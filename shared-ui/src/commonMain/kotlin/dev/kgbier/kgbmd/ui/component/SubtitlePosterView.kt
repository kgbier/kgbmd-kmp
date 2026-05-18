package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.ui.component.atom.SubtitleText

@Composable
fun SubtitlePosterView(
    poster: MoviePoster,
    subtitle: String?,
    onClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .widthIn(max = 100.dp)
    ) {
        PosterView(
            poster = poster,
            onClick = onClick,
        )
        subtitle?.let { subtitle ->
            SubtitleText(
                text = subtitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
