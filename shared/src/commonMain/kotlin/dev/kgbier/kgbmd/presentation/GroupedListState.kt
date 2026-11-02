package dev.kgbier.kgbmd.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupedListState<TGroupId, TGrouping, TItem>(
    private val scope: CoroutineScope,
    private val delegate: Delegate<TGroupId, TGrouping, TItem>,
) {

    interface Delegate<TGroupId, TGrouping, TItem> {
        suspend fun fetchGroups(): List<TGrouping>
        suspend fun fetchItems(groupId: TGroupId): List<TItem>
    }

    enum class GroupState {
        Loading,
        Finished,
    }

    /*
     * A list of all separate Groups
     */
    val groups: StateFlow<List<TGrouping>>
        field = MutableStateFlow<List<TGrouping>>(emptyList())

    /*
     * A map of all Groups and their list of Items
     */
    val groupedItems: StateFlow<Map<TGroupId, List<TItem>>>
        field = MutableStateFlow<Map<TGroupId, List<TItem>>>(emptyMap())

    /*
     * A set of all groups that are expanded
     */
    val expandedGroups: StateFlow<Set<TGroupId>>
        field = MutableStateFlow(setOf<TGroupId>())

    /*
     * A map of all Groups and their respective states (Loading, etc.)
     */
    val groupStates: StateFlow<Map<TGroupId, GroupState>>
        field = MutableStateFlow(mapOf<TGroupId, GroupState>())

    fun load() {
        scope.launch {
            val initialGroups = delegate.fetchGroups()
            groups.update { initialGroups }
        }
    }

    fun toggleGroupExpanded(groupId: TGroupId) {
        scope.launch {
            loadGroup(groupId)

            // Toggle expanded state
            expandedGroups.update { set ->
                if (set.contains(groupId)) {
                    set - groupId
                } else {
                    set + groupId
                }
            }
        }
    }

    private fun loadGroup(groupId: TGroupId) {
        scope.launch {
            runCatching {
                groupStates.update { map ->
                    when (map[groupId]) {
                        GroupState.Loading -> return@launch
                        GroupState.Finished -> return@launch
                        null -> map.plus(groupId to GroupState.Loading)
                    }
                }

                delegate.fetchItems(groupId)
            }.onSuccess { items ->
                groupedItems.update { map ->
                    val newMap = map.toMutableMap()
                    val list = (map[groupId] ?: emptyList()) + items
                    newMap[groupId] = list
                    newMap.toMap()
                }
            }

            groupStates.update { map ->
                map.plus(groupId to GroupState.Finished)
            }
        }
    }
}
