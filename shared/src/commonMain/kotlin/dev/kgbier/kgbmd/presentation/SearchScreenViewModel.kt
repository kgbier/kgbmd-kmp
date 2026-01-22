@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.SearchSuggestionType
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import dev.kgbier.kgbmd.util.cooperativelyCancel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlin.time.Duration.Companion.milliseconds

sealed interface RatingState {
    data object Loading : RatingState
    data object None : RatingState
    data class Success(val rating: String) : RatingState
}

data class SuggestionItem(
    val suggestion: Suggestion,
    val ratingState: RatingState,
)

sealed interface SearchAction {
    data object StartSearch : SearchAction
    data class ReceiveSearchResults(val suggestions: List<Suggestion>) : SearchAction
    data class ReceiveRatings(val ratings: Map<MediaEntityId, RatingState>) : SearchAction
}

class SearchScreenViewModel(
    scope: CoroutineScope,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    private data class SearchScreenState(
        val isLoading: Boolean = false,
        val suggestions: List<Suggestion> = emptyList(),
        val ratings: Map<MediaEntityId, RatingState> = emptyMap(),
    )

    sealed interface SearchScreenViewState {
        data object Idle : SearchScreenViewState
        data object Loading : SearchScreenViewState
        data class Items(val items: List<SuggestionItem>) : SearchScreenViewState
    }

    interface Factory {
        fun createSearchScreenViewModelFactory(scope: CoroutineScope): SearchScreenViewModel
    }

    private val resultsJobQueue = Channel<List<Suggestion>>(capacity = 20)
    private val ratingsMap = mutableMapOf<MediaEntityId, RatingState>()

    val searchQuery = MutableStateFlow("")
    private val actionChannel = Channel<SearchAction>()

    val actions: Flow<SearchAction> = run {
        val searchStateFlow: Flow<SearchAction> = searchQuery.mapNotNull {
            when {
                it.isBlank() -> SearchAction.ReceiveSearchResults(emptyList())
                else -> null
            }
        }

        val searchResultsFlow: Flow<SearchAction> = searchQuery
            .debounce(200.milliseconds)
            .filter { it.isNotBlank() }
            .transformLatest { query ->
                emit(SearchAction.StartSearch)

                runCatching { mediaInfoRepo.getSearchResults(query) }
                    .onSuccess {
                        backgroundScope.launch { resultsJobQueue.send(it) }
                        emit(SearchAction.ReceiveSearchResults(it))
                    }
            }

        val ratingResultsFlow = resultsJobQueue.receiveAsFlow()
            .flatMapMerge(concurrency = 16) { it.asFlow() }.buffer()
            .filter { it.type == SearchSuggestionType.Movie || it.type == SearchSuggestionType.TvShow }
            .map { it.id }
            .filterNot { ratingsMap.contains(it) }
            .transform { id ->
                ratingsMap[id] = RatingState.Loading
                emit(Unit)

                runCatching { mediaInfoRepo.getRating(id) }
                    .cooperativelyCancel()
                    .onFailure { ratingsMap.remove(id) }
                    .onSuccess {
                        ratingsMap[id] = when (it) {
                            null -> RatingState.None
                            else -> RatingState.Success(it)
                        }
                    }
                emit(Unit)
            }.map { SearchAction.ReceiveRatings(ratingsMap.toMap()) }


        merge(
            actionChannel.receiveAsFlow(),
            searchResultsFlow,
            searchStateFlow,
            ratingResultsFlow,
        )
    }

    private val state = actions
        .scan(SearchScreenState()) { state, action ->
            when (action) {
                SearchAction.StartSearch -> state.copy(isLoading = true)
                is SearchAction.ReceiveRatings -> state.copy(
                    ratings = action.ratings,
                    isLoading = false,
                )

                is SearchAction.ReceiveSearchResults -> state.copy(suggestions = action.suggestions)
            }
        }.stateIn(backgroundScope, started = SharingStarted.WhileSubscribed(), SearchScreenState())

    val viewState: StateFlow<SearchScreenViewState> = state.map { state ->
        when {
            state.isLoading && state.suggestions.isEmpty() -> SearchScreenViewState.Loading
            state.suggestions.isNotEmpty() -> SearchScreenViewState.Items(
                state.suggestions.map {
                    SuggestionItem(it, state.ratings[it.id] ?: RatingState.None)
                }
            )

            else -> SearchScreenViewState.Idle
        }
    }.stateIn(
        backgroundScope,
        started = SharingStarted.WhileSubscribed(),
        SearchScreenViewState.Idle
    )

    fun search(query: String) {
        backgroundScope.launch { searchQuery.emit(query) }
    }
}
