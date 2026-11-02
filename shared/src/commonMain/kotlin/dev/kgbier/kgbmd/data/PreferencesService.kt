package dev.kgbier.kgbmd.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.kgbier.kgbmd.data.datastore.DatastorePreferencesHolder
import dev.kgbier.kgbmd.data.datastore.flow
import dev.kgbier.kgbmd.data.datastore.set
import dev.kgbier.kgbmd.domain.model.Country
import dev.kgbier.kgbmd.domain.model.TitleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferencesService {
    val mainTitleCategory: Flow<TitleCategory?>
    suspend fun updateMainTitleCategory(titleCategory: TitleCategory)

    val postcode: Flow<String?>
    suspend fun updatePostcode(postcode: String?)

    val country: Flow<Country?>
    suspend fun updateCountry(country: Country?)
}

class PreferencesDatastore(
    override val datastore: DataStore<Preferences>,
) : PreferencesService, DatastorePreferencesHolder {

    private val titleCategoryKey = stringPreferencesKey("title_category")
    private val postcodeKey = stringPreferencesKey("postcode")
    private val countryKey = stringPreferencesKey("country")

    // Getters/Setters

    override val mainTitleCategory: Flow<TitleCategory?> = titleCategoryKey.flow()
        .map { it?.toTitleCategory() }

    override suspend fun updateMainTitleCategory(titleCategory: TitleCategory) =
        titleCategoryKey.set(titleCategory.toData())

    override val postcode: Flow<String?> = postcodeKey.flow()

    override suspend fun updatePostcode(postcode: String?) = postcodeKey.set(postcode)

    override val country: Flow<Country?> = countryKey.flow()
        .map { it?.let(::Country) }

    override suspend fun updateCountry(country: Country?) =
        countryKey.set(country?.alpha2)

    // Mappers

    private fun TitleCategory.toData(): String = when (this) {
        TitleCategory.Movie -> "movie"
        TitleCategory.TvShow -> "tvshow"
    }

    private fun String.toTitleCategory(): TitleCategory = when (this) {
        "movie" -> TitleCategory.Movie
        "tvshow" -> TitleCategory.TvShow
        else -> error("Expected a valid TitleCategory, found $this")
    }
}
