package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.data.PreferencesService
import dev.kgbier.kgbmd.domain.model.TitleCategory
import kotlinx.coroutines.flow.Flow

class PreferencesRepo(
    private val prefsService: PreferencesService,
) {
    fun subscribeToTitleCategory(): Flow<TitleCategory> = prefsService.mainTitleCategory

    suspend fun setSavedTitleCategory(
        titleCategory: TitleCategory,
    ) = prefsService.updateMainTitleCategory(titleCategory)
}
