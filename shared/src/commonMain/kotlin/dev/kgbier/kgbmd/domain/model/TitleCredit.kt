package dev.kgbier.kgbmd.domain.model

data class TitleCredit(
    val moviePoster: MoviePoster,
    val year: String?,
    val productionStatus: String?,
    val roles: List<String>,
)
