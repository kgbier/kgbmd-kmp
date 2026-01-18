package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import dev.kgbier.kgbmd.util.cooperativelyCancel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MovieListViewModelFactory(
    private val preferencesRepo: PreferencesRepo
) : MovieListViewModel.Factory {
    override fun createMovieListViewModelFactory(scope: CoroutineScope): MovieListViewModel =
        MovieListViewModel(scope = scope, preferencesRepo = preferencesRepo)
}

class MovieListViewModel(
    private val scope: CoroutineScope,
    private val preferencesRepo: PreferencesRepo
) {

    interface Factory {
        fun createMovieListViewModelFactory(scope: CoroutineScope): MovieListViewModel
    }

    sealed interface TitleListState {
        data object Loading : TitleListState
        data class Loaded(val items: List<MoviePoster>) : TitleListState
    }

    val titleCategory: MutableStateFlow<TitleCategory> =
        MutableStateFlow(preferencesRepo.getSavedTitleCategory())
    val isSpinnerShown: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val titleList: MutableStateFlow<TitleListState> = MutableStateFlow(TitleListState.Loading)

    init {
        load(titleCategory.value)
    }

    fun reload() = load(titleCategory.value)

    private fun load(titleCategory: TitleCategory) = scope.launch {
        runCatching {
            when (titleCategory) {
                TitleCategory.MOVIE -> titleList.value =
                    TitleListState.Loaded(MediaInfoRepo.getMovieHotListPosters())

                TitleCategory.TV_SHOW -> titleList.value =
                    TitleListState.Loaded(MediaInfoRepo.getTvShowHotListPosters())
            }
        }.cooperativelyCancel()
        isSpinnerShown.value = false
    }

    fun toggleTitleCategory() {
        val newCategory = when (titleCategory.value) {
            TitleCategory.MOVIE -> TitleCategory.TV_SHOW
            TitleCategory.TV_SHOW -> TitleCategory.MOVIE
        }

        isSpinnerShown.value = true
        titleList.value = TitleListState.Loading
        load(newCategory)

        preferencesRepo.setSavedTitleCategory(newCategory)
        titleCategory.value = newCategory
    }
}
