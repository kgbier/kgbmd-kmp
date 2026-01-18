package dev.kgbier.kgbmd.domain.model

import dev.kgbier.kgbmd.data.imdb.model.RatingResponse
import kotlin.math.pow
import kotlin.math.roundToInt

fun transformRatingResponse(response: RatingResponse) = transformRatingInfo(response.ratingInfo)

fun transformRatingInfo(info: RatingResponse.RatingInfo) =
    info.rating?.takeIf { it > 0 }?.toOneDecimalPlace()

/**
 * A multiplatform replacement for "String.format("%.1f", it)
 */
private fun Float.toOneDecimalPlace(): String {
    val factor = 10.0.pow(1) // For one decimal place
    return ((this * factor).roundToInt() / factor).toString()
}