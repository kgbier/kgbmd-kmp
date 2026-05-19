package dev.kgbier.kgbmd.data

import dev.kgbier.kgbmd.domain.model.TitleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface PreferencesService {
    val mainTitleCategory: Flow<TitleCategory>
    suspend fun updateMainTitleCategory(titleCategory: TitleCategory)
}

class StubPreferencesService : PreferencesService {
    private val titleCategoryState = MutableStateFlow(TitleCategory.Movie)

    override val mainTitleCategory: Flow<TitleCategory> = titleCategoryState

    override suspend fun updateMainTitleCategory(titleCategory: TitleCategory) {
        titleCategoryState.emit(titleCategory)
    }
}
