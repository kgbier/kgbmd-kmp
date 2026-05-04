package dev.kgbier.kgbmd.domain.model

sealed interface MediaEntityDetails

data class NameDetails(
    val name: String,
    val headshot: Image?,
    val description: String?,
    val topCredits: List<TopCredit>,
) : MediaEntityDetails {
    data class TopCredit(
        val id: MediaEntityId,
        val name: String,
        val poster: Image?,
        val year: String?,
        val rating: String?,
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
    val topCast: TopCast?,
) : MediaEntityDetails {
    data class Rating(val value: String, val best: String, val count: String?)

    data class TopCast(
        val topCast: List<CastCredit>,
        val isMore: Boolean,
        val groupingId: CreditGroupingId,
    )

    data class CastCredit(
        val nameProfile: NameProfile,
        val roles: List<String>,
    )
}
