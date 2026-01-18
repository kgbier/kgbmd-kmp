package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class TitleListViewModelFactory(
    private val preferencesRepo: PreferencesRepo
) : TitleListViewModel.Factory {
    override fun createTitleListViewModelFactory(scope: CoroutineScope): TitleListViewModel =
        TitleListViewModel(scope = scope, preferencesRepo = preferencesRepo)
}

class TitleListViewModel(
    private val scope: CoroutineScope, preferencesRepo: PreferencesRepo
) {

    interface Factory {
        fun createTitleListViewModelFactory(scope: CoroutineScope): TitleListViewModel
    }

    sealed interface TitleListState {
        data object Loading : TitleListState
        data class Loaded(val items: List<MoviePoster>) : TitleListState
    }

    val titleCategory: Flow<TitleCategory> = preferencesRepo.subscribeToTitleCategory().shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    val titleList: MutableStateFlow<TitleListState> = MutableStateFlow(TitleListState.Loading)

    init {
        titleCategory.onEach {
            titleList.value = TitleListState.Loading
            load(it)
        }.launchIn(scope)
    }

    private suspend fun load(titleCategory: TitleCategory) {
        runCatching {
            when (titleCategory) {
                TitleCategory.Movie -> titleList.value =
                    TitleListState.Loaded(MediaInfoRepo.getMovieHotListPosters())

                TitleCategory.TvShow -> titleList.value =
                    TitleListState.Loaded(MediaInfoRepo.getTvShowHotListPosters())
            }
        }
    }
}
