package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.newCoroutineContext

sealed interface GroupedCreditsForTitleListUiState {
    data object Loading : GroupedCreditsForTitleListUiState
    data class Items(val items: List<GroupedCreditsForTitleListItem>) : GroupedCreditsForTitleListUiState
    data class Error(val error: Throwable) : GroupedCreditsForTitleListUiState
}

sealed interface GroupedCreditsForTitleListItem {
    val groupingId: CreditGroupingId

    data class Grouping(val grouping: CreditGrouping) : GroupedCreditsForTitleListItem {
        override val groupingId: CreditGroupingId
            get() = grouping.id
    }

    data class Credit(
        override val groupingId: CreditGroupingId,
        val credit: CastCredit,
    ) : GroupedCreditsForTitleListItem

    data class Loading(
        override val groupingId: CreditGroupingId,
    ) : GroupedCreditsForTitleListItem
}

class GroupedCreditsForTitleViewModel(
    scope: CoroutineScope,
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    interface Factory {
        fun createGroupedCreditsForTitleViewModel(
            scope: CoroutineScope,
            id: MediaEntityId
        ): GroupedCreditsForTitleViewModel
    }

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    private inner class GroupedListStateDelegate :
        GroupedListState.Delegate<CreditGroupingId, CreditGrouping, CastCredit> {
        override suspend fun fetchGroups(): List<CreditGrouping> {
            return mediaInfoRepo.getCreditGroupsForTitle(id)
        }

        override suspend fun fetchItems(groupId: CreditGroupingId): List<CastCredit> {
            return mediaInfoRepo.getCreditsForTitleGroup(id, groupId)
        }
    }

    val groupedCreditsState = GroupedListState(
        scope = backgroundScope,
        delegate = GroupedListStateDelegate(),
    )

    val expandedGroups
        get() = groupedCreditsState.expandedGroups

    val state: StateFlow<GroupedCreditsForTitleListUiState> = combine(
        groupedCreditsState.groups,
        groupedCreditsState.groupedCredits,
        groupedCreditsState.groupStates,
    ) { groups, groupedCredits, groupStates ->
        if (groups.isNotEmpty()) {
            val list = mutableListOf<GroupedCreditsForTitleListItem>()
            groups.forEach { currentGroup ->
                list.add(GroupedCreditsForTitleListItem.Grouping(currentGroup))
                groupedCredits[currentGroup.id]?.forEach { currentCredit ->
                    list.add(
                        GroupedCreditsForTitleListItem.Credit(
                            groupingId = currentGroup.id,
                            credit = currentCredit,
                        )
                    )
                }
                if (groupStates[currentGroup.id] == GroupedListState.GroupState.Loading) {
                    list.add(GroupedCreditsForTitleListItem.Loading(currentGroup.id))
                }
            }
            GroupedCreditsForTitleListUiState.Items(list.toList())
        } else GroupedCreditsForTitleListUiState.Loading
    }.catch {
        emit(GroupedCreditsForTitleListUiState.Error(it))
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GroupedCreditsForTitleListUiState.Loading,
    )

    fun load() = groupedCreditsState.load()

    fun toggleGroupExpanded(groupId: CreditGroupingId) =
        groupedCreditsState.toggleGroupExpanded(groupId)
}
