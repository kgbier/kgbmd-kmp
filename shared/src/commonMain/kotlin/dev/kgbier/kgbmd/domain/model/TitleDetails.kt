package dev.kgbier.kgbmd.domain.model

sealed interface MediaEntityDetails

data class NameDetails(
    val name: String,
    val headshot: Image?,
    val description: String?,
    val filmography: Map<FilmographicCategory, List<Title>>,
) : MediaEntityDetails {
    data class Title(
        val id: MediaEntityId,
        val name: String?,
        val year: String?,
        val role: String?,
    )
}

data class TitleDetails(
    val name: String,
    val poster: Image?,
    val contentRating: String?,
    val genre: String,
    val principalCreditsByGroup: Map<String, List<PrincipalCredit>>,
    val description: String?,
    val yearReleased: String?,
    val rating: Rating?,
    val duration: String?,
    val castMembers: List<CastMember>,
) : MediaEntityDetails {
    data class Rating(val value: String, val best: String, val count: String?)

    data class PrincipalCredit(
        val id: MediaEntityId,
        val name: String,
        val thumbnailUrl: String?,
    )

    data class CastMember(
        val thumbnailUrl: String?,
        val name: String?,
        val role: String?,
        val nameId: String?,
    )
}
