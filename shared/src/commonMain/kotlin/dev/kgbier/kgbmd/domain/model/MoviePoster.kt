package dev.kgbier.kgbmd.domain.model

data class MoviePoster(
    val id: MediaEntityId,
    val title: String,
    val rating: String?,
    val image: Image?,
)
