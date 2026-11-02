package dev.kgbier.kgbmd.data.imdb.graphql.model

import dev.kgbier.kgbmd.domain.model.Location
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * The location of the client. A lat/long or postal code location must be given for the client input to be valid. If both
 * lat/long and postal code are provided we default to using lat/long.
 */
@Serializable
data class WatchOptionsLocation(
    val latLong: LatLong?,
    val postalCodeLocation: PostalCodeLocation?,
)

/**
 * The location of the client by postal code. The country field is an ISO 3166-1 alpha-2 country code (ex. US or GB).
 * The postalCode field is alpha numeric (ex. 98109 or D-11179).
 */
@Serializable
data class PostalCodeLocation(
    val country: String,
    val postalCode: String,
)

/**
 * Represents a location in the latitude/longitude format.
 * Values should not be more precise than one hundredth of a degree. More precise inputs will be
 * treated as invalid inputs.
 */
@Serializable
data class LatLong(
    val lat: LocationDecimal,
    val long: LocationDecimal,
)

/**
 * A decimal used for a latitude or longitude degree value. If more granular location data is given than one hundredth of a degree, the request will be rejected.
 */
@Serializable
@JvmInline
value class LocationDecimal(
    val value: String
)

fun Location.toWatchOptionsLocation(): WatchOptionsLocation {
    return when (this) {
        is Location.CountryPostcode -> WatchOptionsLocation(
            latLong = null,
            postalCodeLocation = PostalCodeLocation(
                country = country.alpha2,
                postalCode = postcode,
            ),
        )

        is Location.LatLong -> WatchOptionsLocation(
            latLong = LatLong(
                lat = latitude.toLocationDecimal(),
                long = longitude.toLocationDecimal(),
            ),
            postalCodeLocation = null,
        )
    }
}

/**
 * TODO: Truncate to one hundredth of a degree
 */
fun Double.toLocationDecimal(): LocationDecimal = LocationDecimal(toString())
