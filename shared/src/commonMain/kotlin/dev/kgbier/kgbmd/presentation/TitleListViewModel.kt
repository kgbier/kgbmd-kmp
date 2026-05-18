package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TitleListViewModel(
    private val scope: CoroutineScope,
    preferencesRepo: PreferencesRepo,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    interface Factory {
        fun createTitleListViewModelFactory(scope: CoroutineScope): TitleListViewModel
    }

    sealed interface TitleListState {
        data object Loading : TitleListState
        data class Error(val error: Throwable) : TitleListState
        data class Loaded(val items: List<MoviePoster>) : TitleListState
    }

    val titleCategory: Flow<TitleCategory> = preferencesRepo.subscribeToTitleCategory().shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    val titleList: MutableStateFlow<TitleListState> = MutableStateFlow(TitleListState.Loading)

    private val retryChannel = Channel<Unit>()
    private val reloadFlow = retryChannel.receiveAsFlow().onStart { emit(Unit) }

    init {
        titleCategory.combineTransform(reloadFlow) { a, _ ->
            emit(a)
        }.onEach {
            titleList.value = TitleListState.Loading
            load(it)
        }.launchIn(scope)
    }

    fun retry() {
        scope.launch { retryChannel.send(Unit) }
    }

    private suspend fun load(titleCategory: TitleCategory) {
        val state = runCatching {
            when (titleCategory) {
                TitleCategory.Movie ->
                    mediaInfoRepo.getMovieHotListPosters(null)

                TitleCategory.TvShow ->
                    mediaInfoRepo.getTvShowHotListPosters(null)
            }
        }.fold(
            onSuccess = { TitleListState.Loaded(it) },
            onFailure = {
                delay(200.milliseconds) // Avoid flicker
                TitleListState.Error(it)
            },
        )

        titleList.value = state
    }
}
