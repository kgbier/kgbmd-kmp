package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.data.PreferencesService
import dev.kgbier.kgbmd.domain.model.TitleCategory

class PreferencesRepo(
    private val prefsService: PreferencesService,
) {
    fun getSavedTitleCategory() = prefsService.getMainTitleCategory()
    fun setSavedTitleCategory(titleCategory: TitleCategory) =
        prefsService.setMainTitleCategory(titleCategory)
}
