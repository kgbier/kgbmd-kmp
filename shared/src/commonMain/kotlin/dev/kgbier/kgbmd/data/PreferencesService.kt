package dev.kgbier.kgbmd.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.kgbier.kgbmd.domain.model.TitleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferencesService {
    val mainTitleCategory: Flow<TitleCategory>
    suspend fun updateMainTitleCategory(titleCategory: TitleCategory)
}

class PreferencesDatastore(
    val datastore: DataStore<Preferences>,
) : PreferencesService {

    override val mainTitleCategory: Flow<TitleCategory> = datastore.data.map { preferences ->
        preferences[titleCategoryKey]?.toTitleCategory() ?: TitleCategory.Movie
    }

    override suspend fun updateMainTitleCategory(titleCategory: TitleCategory) {
        datastore.edit { preferences ->
            preferences[titleCategoryKey] = titleCategory.toData()
        }
    }

    private val titleCategoryKey = stringPreferencesKey("title_category")

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
