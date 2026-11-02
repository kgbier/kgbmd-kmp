package dev.kgbier.kgbmd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class MainScreenViewModel(
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    val backgroundScope = viewModelScope + Dispatchers.Default

    val titleCategory: Flow<TitleCategory> = preferencesRepo.subscribeToTitleCategory().shareIn(
        scope = backgroundScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    fun toggleTitleCategory() = backgroundScope.launch {
        val newCategory = when (titleCategory.first()) {
            TitleCategory.Movie -> TitleCategory.TvShow
            TitleCategory.TvShow -> TitleCategory.Movie
        }

        preferencesRepo.setSavedTitleCategory(newCategory)
    }
}
