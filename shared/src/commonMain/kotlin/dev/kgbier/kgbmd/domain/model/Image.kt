package dev.kgbier.kgbmd.domain.model

import dev.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import kotlin.jvm.JvmInline

@JvmInline
value class Image(val rawUrl: String) {
    val hintUrl: String
        get() = ImageResizer.resize(rawUrl, ImageResizer.SIZE_WIDTH_HINT)

    val thumbnailUrl: String
        get() = ImageResizer.resize(rawUrl, ImageResizer.SIZE_WIDTH_THUMBNAIL_CDN)

    val largeUrl: String
        get() = ImageResizer.resize(rawUrl, ImageResizer.SIZE_WIDTH_LARGE)

    val originalUrl: String
        get() = ImageResizer.resize(rawUrl, ImageResizer.SIZE_FULL)
}
