package dev.kgbier.kgbmd.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _groups = MutableStateFlow<List<TGrouping>>(emptyList())
    val groups = _groups.asStateFlow()

    /*
     * A map of all Groups and their list of Items
     */
    private val _groupedCredits =
        MutableStateFlow<Map<TGroupId, List<TItem>>>(emptyMap())
    val groupedCredits = _groupedCredits.asStateFlow()

    /*
     * A set of all groups that are expanded
     */
    private val _expandedGroups = MutableStateFlow(setOf<TGroupId>())
    val expandedGroups: StateFlow<Set<TGroupId>> get() = _expandedGroups

    /*
     * A map of all Groups and their respective states (Loading, etc.)
     */
    private val _groupStates = MutableStateFlow(mapOf<TGroupId, GroupState>())
    val groupStates = _groupStates.asStateFlow()

    fun load() {
        scope.launch {
            val initialGroups = delegate.fetchGroups()
            _groups.update { initialGroups }
        }
    }

    fun toggleGroupExpanded(groupId: TGroupId) {
        scope.launch {
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

    private fun loadGroup(groupId: TGroupId) {
        scope.launch {
            runCatching {
                _groupStates.update { map ->
                    when (map[groupId]) {
                        GroupState.Loading -> return@launch
                        GroupState.Finished -> return@launch
                        null -> map.plus(groupId to GroupState.Loading)
                    }
                }

                delegate.fetchItems(groupId)
            }.onSuccess { items ->
                _groupedCredits.update { map ->
                    val newMap = map.toMutableMap()
                    val list = (map[groupId] ?: emptyList()) + items
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
