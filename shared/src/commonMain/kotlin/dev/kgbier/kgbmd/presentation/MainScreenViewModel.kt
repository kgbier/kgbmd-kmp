package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MainScreenViewModelFactory(
    private val preferencesRepo: PreferencesRepo
) : MainScreenViewModel.Factory {
    override fun createMainScreenViewModelFactory(scope: CoroutineScope): MainScreenViewModel =
        MainScreenViewModel(scope = scope, preferencesRepo = preferencesRepo)
}

class MainScreenViewModel(
    private val scope: CoroutineScope,
    private val preferencesRepo: PreferencesRepo
) {

    interface Factory {
        fun createMainScreenViewModelFactory(scope: CoroutineScope): MainScreenViewModel
    }

    val titleCategory: Flow<TitleCategory> = preferencesRepo.subscribeToTitleCategory().shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    fun toggleTitleCategory() = scope.launch {
        val newCategory = when (titleCategory.first()) {
            TitleCategory.Movie -> TitleCategory.TvShow
            TitleCategory.TvShow -> TitleCategory.Movie
        }

        preferencesRepo.setSavedTitleCategory(newCategory)
    }
}
