package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import dev.kgbier.kgbmd.ui.RatingStarView

@Composable
fun HeroRatingView(
    rating: String,
    bestRating: String,
    ratings: String? = null,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = rating, style = MaterialTheme.typography.titleLarge
        )
        RatingStarView(
            ratingText = " / $bestRating",
            textStyle = MaterialTheme.typography.titleMedium,
            textColour = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    ratings?.let { count ->
        Text(
            text = count,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
fun HeroRatingViewPreview() = MaterialTheme {
    HeroRatingView(
        rating = "7.9",
        bestRating = "10",
        ratings = "1234",
    )
}
