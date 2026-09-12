package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import kotlinx.serialization.Serializable

@Serializable
data class TitlePosterFragment(
    val id: String,
    val titleText: TitleText,
    val primaryImage: PrimaryImage?,
    val ratingsSummary: RatingsSummary,
) {
    @Serializable
    data class TitleText(val text: String)

    @Serializable
    data class PrimaryImage(val url: String)

    @Serializable
    data class RatingsSummary(val aggregateRating: Double?)

    companion object {
        const val name = "TitlePosterFragment"
        const val fragment = """
fragment $name on Title {
  id
  primaryImage {
    url
  }
  titleText {
    text
  }
  ratingsSummary {
    aggregateRating
  }
}
"""
    }
}

fun TitlePosterFragment.toMoviePoster() = MoviePoster(
    id = MediaEntityId(id),
    title = titleText.text,
    rating = ratingsSummary.aggregateRating
        ?.takeUnless { it == 0.0 }?.toString(),
    image = primaryImage?.url?.let(::transformImageUrl),
)
