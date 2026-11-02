package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleEpisode
import dev.kgbier.kgbmd.domain.model.TitleSeason
import dev.kgbier.kgbmd.domain.model.TitleSeasonId
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.newCoroutineContext

sealed interface GroupedEpisodesForTitleListUiState {
    data object Loading : GroupedEpisodesForTitleListUiState
    data class Items(val items: List<GroupedEpisodesForTitleListItem>) :
        GroupedEpisodesForTitleListUiState

    data class Error(val error: Throwable) : GroupedEpisodesForTitleListUiState
}

sealed interface GroupedEpisodesForTitleListItem {
    val groupingId: TitleSeasonId

    data class Grouping(val grouping: TitleSeason) : GroupedEpisodesForTitleListItem {
        override val groupingId: TitleSeasonId
            get() = grouping.id
    }

    data class Episode(
        override val groupingId: TitleSeasonId,
        val episode: TitleEpisode,
    ) : GroupedEpisodesForTitleListItem

    data class Loading(
        override val groupingId: TitleSeasonId,
    ) : GroupedEpisodesForTitleListItem
}

class GroupedEpisodesForTitleViewModel(
    scope: CoroutineScope,
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    interface Factory {
        fun createGroupedEpisodesForTitleViewModel(
            scope: CoroutineScope,
            id: MediaEntityId
        ): GroupedEpisodesForTitleViewModel
    }

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    private inner class GroupedListStateDelegate :
        GroupedListState.Delegate<TitleSeasonId, TitleSeason, TitleEpisode> {
        override suspend fun fetchGroups(): List<TitleSeason> {
            return mediaInfoRepo.getEpisodeSeasonsForTitle(id)
        }

        override suspend fun fetchItems(groupId: TitleSeasonId): List<TitleEpisode> {
            return mediaInfoRepo.getEpisodesForTitleSeason(id, groupId)
        }
    }

    val groupedEpisodesState = GroupedListState(
        scope = backgroundScope,
        delegate = GroupedListStateDelegate(),
    )

    val expandedGroups
        get() = groupedEpisodesState.expandedGroups

    val state: StateFlow<GroupedEpisodesForTitleListUiState> = combine(
        groupedEpisodesState.groups,
        groupedEpisodesState.groupedItems,
        groupedEpisodesState.groupStates,
    ) { groups, groupedEpisodes, groupStates ->
        if (groups.isNotEmpty()) {
            val list = mutableListOf<GroupedEpisodesForTitleListItem>()
            groups.forEach { currentGroup ->
                list.add(GroupedEpisodesForTitleListItem.Grouping(currentGroup))
                groupedEpisodes[currentGroup.id]?.forEach { currentEpisode ->
                    list.add(
                        GroupedEpisodesForTitleListItem.Episode(
                            groupingId = currentGroup.id,
                            episode = currentEpisode,
                        )
                    )
                }
                if (groupStates[currentGroup.id] == GroupedListState.GroupState.Loading) {
                    list.add(GroupedEpisodesForTitleListItem.Loading(currentGroup.id))
                }
            }
            GroupedEpisodesForTitleListUiState.Items(list.toList())
        } else GroupedEpisodesForTitleListUiState.Loading
    }.catch {
        emit(GroupedEpisodesForTitleListUiState.Error(it))
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GroupedEpisodesForTitleListUiState.Loading,
    )

    fun load() = groupedEpisodesState.load()

    fun toggleGroupExpanded(groupId: TitleSeasonId) =
        groupedEpisodesState.toggleGroupExpanded(groupId)
}
