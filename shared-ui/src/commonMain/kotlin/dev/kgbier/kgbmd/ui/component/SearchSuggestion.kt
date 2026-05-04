package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.SearchSuggestionType
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.presentation.RatingState
import dev.kgbier.kgbmd.ui.component.atom.Portrait
import dev.kgbier.kgbmd.ui.component.molecule.RatingStarView
import dev.kgbier.kgbmd.ui.theme.AppTheme

@Composable
fun SearchSuggestion(
    suggestion: Suggestion,
    ratingState: RatingState,
    modifier: Modifier = Modifier,
) = with(suggestion) {
    SearchSuggestion(
        title = title,
        suggestionType = type,
        year = year,
        tidbit = tidbit,
        thumbnailUrl = thumbnailUrl,
        rating = when (ratingState) {
            is RatingState.Success -> ratingState.rating
            else -> null
        },
        isRatingLoading = ratingState == RatingState.Loading,
        modifier = modifier,
    )
}


@Composable
fun SearchSuggestion(
    title: String,
    modifier: Modifier = Modifier,
    suggestionType: SearchSuggestionType? = null,
    year: String? = null,
    tidbit: String? = null,
    thumbnailUrl: String? = null,
    rating: String? = null,
    isRatingLoading: Boolean = false,
) = Row(modifier = modifier) {
    if (thumbnailUrl == null) {
        Spacer(Modifier.size(40.dp))
    } else when (suggestionType) {
        SearchSuggestionType.CastOrCrew -> Portrait(
            creditImageUrl = thumbnailUrl,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically)
        )

        SearchSuggestionType.Movie,
        SearchSuggestionType.Game,
        SearchSuggestionType.TvShow,
        null,
            -> PosterView(
            thumbnailUrl = null,
            imageUrl = thumbnailUrl,
            onClick = { },
            enabled = false,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .size(40.dp, 58.dp)
                .align(Alignment.CenterVertically)
        )
    }
    Spacer(Modifier.width(16.dp))
    Column(
        Modifier
            .weight(1f)
            .align(Alignment.CenterVertically)
    ) {
        Row {
            val heading = buildAnnotatedString {
                append(title)
                year?.let {
                    pushStyle(
                        MaterialTheme.typography.bodySmall.toSpanStyle()
                            .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    append("${Typography.nbsp}(${year})")
                }
            }
            Text(
                text = heading,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            if (rating != null || isRatingLoading) {
                Spacer(Modifier.size(8.dp))
                RatingStarView(
                    ratingText = rating ?: "",
                    isSpinning = isRatingLoading,
                    textStyle = MaterialTheme.typography.titleSmall
                )
            }
        }
        tidbit?.let {
            Text(
                text = tidbit,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
@Preview
fun SearchSuggestionPreview() = AppTheme {
    SearchSuggestion("title")
}
