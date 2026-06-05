package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _groups = MutableStateFlow<List<CreditGrouping>>(emptyList())
    private val _groupedCredits =
        MutableStateFlow<Map<CreditGroupingId, List<CastCredit>>>(emptyMap())

    private val _expandedGroups = MutableStateFlow(setOf<CreditGroupingId>())
    val expandedGroups: StateFlow<Set<CreditGroupingId>> get() = _expandedGroups

    private enum class GroupState {
        Loading,
        Finished,
    }

    private val _groupStates = MutableStateFlow(mapOf<CreditGroupingId, GroupState>())

    val state: StateFlow<CreditGroupUiState> = combine(
        _groups,
        _groupedCredits,
        _groupStates,
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
                if (groupStates[currentGroup.id] == GroupState.Loading) {
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

    fun load() {
        backgroundScope.launch {
            val initialGroups = mediaInfoRepo.getCreditGroupsForTitle(id)
            _groups.update { initialGroups }
        }
    }

    fun toggleGroupExpanded(groupId: CreditGroupingId) {
        backgroundScope.launch {
            loadGroup(groupId)
            // Toggle expanded state
            _expandedGroups.update { set ->
                if (set.contains(groupId)) {
                    set - groupId
                } else {
                    set + groupId
                }
            }
        }
    }

    private fun loadGroup(groupId: CreditGroupingId) {
        backgroundScope.launch {
            runCatching {
                _groupStates.update { map ->
                    when (map[groupId]) {
                        GroupState.Loading -> return@launch
                        GroupState.Finished -> return@launch
                        null -> map.plus(groupId to GroupState.Loading)
                    }
                }

                mediaInfoRepo.getCreditsForTitleGroup(id, groupId)
            }.onSuccess { credits ->
                _groupedCredits.update { map ->
                    val newMap = map.toMutableMap()
                    val list = (map[groupId] ?: emptyList()) + credits
                    newMap[groupId] = list
                    newMap.toMap()
                }
            }

            _groupStates.update { map ->
                map.plus(groupId to GroupState.Finished)
            }
        }
    }
}
