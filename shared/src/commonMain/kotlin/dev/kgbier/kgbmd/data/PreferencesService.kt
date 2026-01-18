package dev.kgbier.kgbmd.data

import dev.kgbier.kgbmd.domain.model.TitleCategory

interface PreferencesService {
    fun getMainTitleCategory(): TitleCategory
    fun setMainTitleCategory(titleCategory: TitleCategory)
}

class StubPreferencesService : PreferencesService {
    override fun getMainTitleCategory(): TitleCategory = TitleCategory.MOVIE

    override fun setMainTitleCategory(titleCategory: TitleCategory) = Unit
}
