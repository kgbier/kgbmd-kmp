package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.data.PreferencesService
import dev.kgbier.kgbmd.domain.model.Country
import dev.kgbier.kgbmd.domain.model.Location
import dev.kgbier.kgbmd.domain.model.TitleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class PreferencesRepo(
    private val prefsService: PreferencesService,
) {
    fun subscribeToTitleCategory(): Flow<TitleCategory> =
        prefsService.mainTitleCategory.map { it ?: TitleCategory.Movie }

    suspend fun setSavedTitleCategory(
        titleCategory: TitleCategory,
    ) = prefsService.updateMainTitleCategory(titleCategory)

    fun subscribeToPostcode(): Flow<String?> = prefsService.postcode

    suspend fun setSavedPostcode(postcode: String?) = prefsService.updatePostcode(postcode)

    fun subscribeToCountry(): Flow<Country?> = prefsService.country

    suspend fun setSavedCountry(country: Country?) = prefsService.updateCountry(country)

    fun subscribeToLocation(): Flow<Location?> = combine(
        prefsService.country,
        prefsService.postcode,
    ) { country, postcode ->
        if (country != null && postcode != null) {
            Location.CountryPostcode(country = country, postcode = postcode)
        } else null
    }

    suspend fun setSavedLocation(location: Location?) {
        when (location) {
            is Location.LatLong -> TODO()
            is Location.CountryPostcode -> {
                prefsService.updateCountry(location.country)
                prefsService.updatePostcode(location.postcode)
            }

            null -> {
                prefsService.updateCountry(null)
                prefsService.updatePostcode(null)
            }
        }
    }
}
