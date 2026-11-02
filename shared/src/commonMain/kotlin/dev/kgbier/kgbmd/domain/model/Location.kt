package dev.kgbier.kgbmd.domain.model

sealed interface Location {
    data class LatLong(
        val latitude: Double,
        val longitude: Double,
    ) : Location

    data class CountryPostcode(
        val country: Country,
        val postcode: String
    ) : Location
}
