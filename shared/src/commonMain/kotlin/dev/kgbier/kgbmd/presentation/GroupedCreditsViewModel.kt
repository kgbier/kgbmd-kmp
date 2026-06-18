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

sealed interface CreditGroupUiState {
    data object Loading : CreditGroupUiState
    data class Items(val items: List<GroupedCreditListItem>) : CreditGroupUiState
    data class Error(val error: Throwable) : CreditGroupUiState
}

sealed interface GroupedCreditListItem {
    val groupingId: CreditGroupingId

    data class Grouping(val grouping: CreditGrouping) : GroupedCreditListItem {
        override val groupingId: CreditGroupingId
            get() = grouping.id
    }

    data class Credit(
        override val groupingId: CreditGroupingId,
        val credit: CastCredit,
    ) : GroupedCreditListItem

    data class Loading(
        override val groupingId: CreditGroupingId,
    ) : GroupedCreditListItem
}

class GroupedCreditsViewModel(
    scope: CoroutineScope,
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    interface Factory {
        fun createGroupedCreditsViewModel(
            scope: CoroutineScope,
            id: MediaEntityId
        ): GroupedCreditsViewModel
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

    val state: StateFlow<CreditGroupUiState> = combine(
        groupedCreditsState.groups,
        groupedCreditsState.groupedCredits,
        groupedCreditsState.groupStates,
    ) { groups, groupedCredits, groupStates ->
        if (groups.isNotEmpty()) {
            val list = mutableListOf<GroupedCreditListItem>()
            groups.forEach { currentGroup ->
                list.add(GroupedCreditListItem.Grouping(currentGroup))
                groupedCredits[currentGroup.id]?.forEach { currentCredit ->
                    list.add(
                        GroupedCreditListItem.Credit(
                            groupingId = currentGroup.id,
                            credit = currentCredit,
                        )
                    )
                }
                if (groupStates[currentGroup.id] == GroupedListState.GroupState.Loading) {
                    list.add(GroupedCreditListItem.Loading(currentGroup.id))
                }
            }
            CreditGroupUiState.Items(list.toList())
        } else CreditGroupUiState.Loading
    }.catch {
        emit(CreditGroupUiState.Error(it))
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CreditGroupUiState.Loading,
    )

    fun load() = groupedCreditsState.load()

    fun toggleGroupExpanded(groupId: CreditGroupingId) =
        groupedCreditsState.toggleGroupExpanded(groupId)
}
