package dev.kgbier.kgbmd.data.imdb.model

import dev.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import dev.kgbier.kgbmd.domain.model.Image

fun transformImageUrl(imageUrl: String): Image = imageUrl.let {
    Image(
        hintUrl = ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_HINT),
        thumbnailUrl = ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_THUMBNAIL),
        largeUrl = ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_LARGE),
    )
}
