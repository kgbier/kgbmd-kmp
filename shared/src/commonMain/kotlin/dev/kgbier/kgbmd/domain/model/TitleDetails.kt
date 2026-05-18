package dev.kgbier.kgbmd.domain.model

sealed interface MediaEntityDetails

data class NameDetails(
    val name: String,
    val headshot: Image?,
    val description: String?,
    val topCredits: List<TopCredit>,
) : MediaEntityDetails {
    data class TopCredit(
        val poster: MoviePoster,
        val role: String?,
    )
}

data class TitleDetails(
    val name: String,
    val poster: Image?,
    val contentRating: String?,
    val genre: String,
    val principalCreditsByGroup: Map<String, List<NameProfile>>,
    val description: String?,
    val yearReleased: String?,
    val rating: Rating?,
    val duration: String?,
    val episodeMetadata: EpisodeMetadata?,
    val genres: List<String>,
    val topEpisodes: TopEpisodes?,
    val topCast: TopCast?,
) : MediaEntityDetails {
    data class Rating(val value: String, val best: String, val count: String?)

    data class EpisodeMetadata(
        val seriesTitle: String,
        val season: String,
        val number: String,
    )

    data class TopEpisodes(
        val episodeCount: Int,
        val episodes: List<TopEpisode>,
    )

    data class TopEpisode(
        val moviePoster: MoviePoster,
        val season: String,
        val number: String,
    )

    data class TopCast(
        val castTotal: Int,
        val topCast: List<CastCredit>,
        val hasMore: Boolean,
        val groupingId: CreditGroupingId,
    )

    data class CastCredit(
        val nameProfile: NameProfile,
        val roles: List<String>,
    )
}
