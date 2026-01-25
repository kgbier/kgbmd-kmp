package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.ui.component.HeroRatingView
import dev.kgbier.kgbmd.ui.component.Portrait
import dev.kgbier.kgbmd.ui.component.Tag
import dev.kgbier.kgbmd.ui.theme.Tonal

@Composable
fun NameDetailsHeader(
    name: NameDetails,
    modifier: Modifier = Modifier,
) = DetailsHeader(
    imageUrl = name.headshot?.thumbnailUrl,
    modifier = modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f)
        )

        name.headshot?.thumbnailUrl?.let { thumbnailUrl ->
            Portrait(
                creditImageUrl = thumbnailUrl,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun TitleDetailsHeader(
    title: TitleDetails,
    modifier: Modifier = Modifier,
) = DetailsHeader(
    imageUrl = title.poster?.thumbnailUrl,
    modifier = modifier
) {
    val subtitleStyle = MaterialTheme.typography.titleMedium.toSpanStyle()
        .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)

    val heading = remember(title.name, title.yearReleased) {
        buildAnnotatedString {
            append(title.name)
            title.yearReleased?.let {
                pushStyle(subtitleStyle)
                append("${Typography.nbsp}(${title.yearReleased})")
            }
        }
    }
    Text(
        text = heading,
        style = MaterialTheme.typography.headlineSmall,
    )

    TitleSubheadingDetails(
        contentRating = title.contentRating,
        duration = title.duration,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        title.rating?.let { rating ->
            HeroRatingView(
                rating = rating.value,
                bestRating = rating.best,
                ratings = rating.count,
            )
        }
        title.poster?.thumbnailUrl?.let { thumbnailUrl ->
            PosterView(
                thumbnailUrl = null,
                imageUrl = thumbnailUrl,
                onClick = { },
                enabled = false,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.size(40.dp, 58.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun TitleSubheadingDetails(
    modifier: Modifier = Modifier,
    contentRating: String? = null,
    duration: String? = null,
) = Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
) {
    contentRating?.let { contentRating ->
        Tag { Text(contentRating) }
    }
    duration?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(MaterialTheme.typography.labelSmall.fontSize.value.dp)
            )
            Text(text = it, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun DetailsHeader(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    content: @Composable () -> Unit,
) = Box {
    DetailsHeaderBackground(
        imageUrl = imageUrl,
        modifier = Modifier
            .matchParentSize()
    )
    DetailsHeaderForeground(
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun DetailsHeaderForeground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Column(
    verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
) {
    content()
}

@Composable
fun DetailsHeaderBackground(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) = AsyncImage(
    model = imageUrl,
    contentDescription = null,
    contentScale = ContentScale.Crop,
    colorFilter = ColorFilter.tint(
        color = Tonal.scrimDark.copy(alpha = 0.7f),
        blendMode = BlendMode.Darken,
    ),
    modifier = modifier.blur(
        radiusX = 30.dp,
        radiusY = 30.dp,
    )
)
