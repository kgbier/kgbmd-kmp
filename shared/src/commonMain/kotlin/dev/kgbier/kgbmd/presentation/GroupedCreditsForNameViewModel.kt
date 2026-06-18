package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleCredit
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.newCoroutineContext

sealed interface GroupedCreditsForNameListUiState {
    data object Loading : GroupedCreditsForNameListUiState
    data class Items(val items: List<GroupedCreditsForNameListItem>) :
        GroupedCreditsForNameListUiState

    data class Error(val error: Throwable) : GroupedCreditsForNameListUiState
}

sealed interface GroupedCreditsForNameListItem {
    val groupingId: CreditGroupingId

    data class Grouping(val grouping: CreditGrouping) : GroupedCreditsForNameListItem {
        override val groupingId: CreditGroupingId
            get() = grouping.id
    }

    data class Credit(
        override val groupingId: CreditGroupingId,
        val credit: TitleCredit,
    ) : GroupedCreditsForNameListItem

    data class Loading(
        override val groupingId: CreditGroupingId,
    ) : GroupedCreditsForNameListItem
}

class GroupedCreditsForNameViewModel(
    scope: CoroutineScope,
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    interface Factory {
        fun createGroupedCreditsForNameViewModel(
            scope: CoroutineScope,
            id: MediaEntityId
        ): GroupedCreditsForNameViewModel
    }

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    private inner class GroupedListStateDelegate :
        GroupedListState.Delegate<CreditGroupingId, CreditGrouping, TitleCredit> {
        override suspend fun fetchGroups(): List<CreditGrouping> {
            return mediaInfoRepo.getCreditGroupsForName(id)
        }

        override suspend fun fetchItems(groupId: CreditGroupingId): List<TitleCredit> {
            return mediaInfoRepo.getCreditsForNameGroup(id, groupId)
        }
    }

    val groupedCreditsState = GroupedListState(
        scope = backgroundScope,
        delegate = GroupedListStateDelegate(),
    )

    val expandedGroups
        get() = groupedCreditsState.expandedGroups

    val state: StateFlow<GroupedCreditsForNameListUiState> = combine(
        groupedCreditsState.groups,
        groupedCreditsState.groupedCredits,
        groupedCreditsState.groupStates,
    ) { groups, groupedCredits, groupStates ->
        if (groups.isNotEmpty()) {
            val list = mutableListOf<GroupedCreditsForNameListItem>()
            groups.forEach { currentGroup ->
                list.add(GroupedCreditsForNameListItem.Grouping(currentGroup))
                groupedCredits[currentGroup.id]?.forEach { currentCredit ->
                    list.add(
                        GroupedCreditsForNameListItem.Credit(
                            groupingId = currentGroup.id,
                            credit = currentCredit,
                        )
                    )
                }
                if (groupStates[currentGroup.id] == GroupedListState.GroupState.Loading) {
                    list.add(GroupedCreditsForNameListItem.Loading(currentGroup.id))
                }
            }
            GroupedCreditsForNameListUiState.Items(list.toList())
        } else GroupedCreditsForNameListUiState.Loading
    }.catch {
        emit(GroupedCreditsForNameListUiState.Error(it))
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GroupedCreditsForNameListUiState.Loading,
    )

    fun load() = groupedCreditsState.load()

    fun toggleGroupExpanded(groupId: CreditGroupingId) =
        groupedCreditsState.toggleGroupExpanded(groupId)
}
