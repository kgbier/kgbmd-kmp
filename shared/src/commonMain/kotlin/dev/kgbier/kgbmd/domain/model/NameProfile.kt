package dev.kgbier.kgbmd.domain.model

data class NameProfile(
    val id: MediaEntityId,
    val name: String,
    val photo: Image?,
)
