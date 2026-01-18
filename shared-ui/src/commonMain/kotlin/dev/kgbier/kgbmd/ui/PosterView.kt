package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import dev.kgbier.kgbmd.domain.model.MoviePoster

@Composable
fun PosterView(
    poster: MoviePoster,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = PosterView(
    modifier = modifier,
    thumbnailUrl = poster.thumbnailUrl,
    imageUrl = poster.posterUrlSmall,
    title = poster.title,
    rating = poster.rating,
    onClick = onClick,
)

@Composable
fun PosterView(
    thumbnailUrl: String?,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    rating: String? = null,
) {
    PosterCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Box {
            AsyncImage(
                contentDescription = title ?: "Movie Poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(imageUrl)
                    .build()
            )
            rating?.let {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(
                            horizontal = 6.dp,
                        )
                        .align(Alignment.TopEnd)
                ) {
                    RatingStarView(
                        ratingText = rating,
                        textStyle = MaterialTheme.typography.labelMedium,
                        textColour = Color.White,
                        starSizeMultiplier = 0.6f,
                        modifier = Modifier.padding(start = 2.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            title?.let {
                BottomTitle(
                    title = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                )
            }
        }
    }
}

@Composable
fun PosterCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = Card(
    onClick = onClick,
    shape = MaterialTheme.shapes.medium,
    elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
        pressedElevation = 8.dp,
    ),
    content = content,
    modifier = modifier
        .aspectRatio(100f / 148f) // Poster aspect ratio
)

@Composable
private fun BottomTitle(title: String, modifier: Modifier) = Text(
    text = title,
    modifier = modifier
        .background(
            Brush.verticalGradient(
                0.0f to Color.Transparent,
                0.6f to Color.Black.copy(alpha = 0.7f),
                1.0f to Color.Black.copy(alpha = 1f),
                startY = 0f,
                endY = Float.POSITIVE_INFINITY,
            )
        )
        .padding(6.dp)
        .padding(top = 18.dp),
    color = Color.White,
    textAlign = TextAlign.End,
    style = MaterialTheme.typography.labelSmall,
)

@Composable
@Preview
fun PosterViewPreview() = MaterialTheme {
    Box(Modifier.width(148.dp)) {
        PosterView(
            thumbnailUrl = null,
            imageUrl = null,
            onClick = {},
            title = "Movie Title",
            rating = "7.9",
        )
    }
}
