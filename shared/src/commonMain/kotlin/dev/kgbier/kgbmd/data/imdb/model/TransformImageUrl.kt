package dev.kgbier.kgbmd.data.imdb.model

import dev.kgbier.kgbmd.domain.model.Image

fun transformImageUrl(imageUrl: String): Image = Image(imageUrl)
