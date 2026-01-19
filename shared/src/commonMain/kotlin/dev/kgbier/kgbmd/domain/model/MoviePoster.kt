package dev.kgbier.kgbmd.domain.model

data class MoviePoster(
    val ttId: MediaEntityId,
    val title: String,
    val rating: String?,
    val thumbnailUrl: String?,
    val posterUrlSmall: String?,
    val posterUrlLarge: String?,
)
