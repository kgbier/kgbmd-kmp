package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.newCoroutineContext

class GroupedCreditsViewModel(
    scope: CoroutineScope,
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) {

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    interface Factory {
        fun createGroupedCreditsViewModel(
            scope: CoroutineScope,
            id: MediaEntityId
        ): GroupedCreditsViewModel
    }

    val state: StateFlow<List<CreditGrouping>?> = flow {
        emit(mediaInfoRepo.getCreditGroupsForTitle(id))
    }.stateIn(
        scope = backgroundScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )
}