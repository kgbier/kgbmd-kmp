package dev.kgbier.kgbmd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus

class DetailsScreenViewModel(
    private val id: MediaEntityId,
    private val mediaInfoRepo: MediaInfoRepo,
) : ViewModel() {

    private val backgroundScope = viewModelScope + Dispatchers.Default

    val state: StateFlow<MediaEntityDetails?> = flow {
        emit(mediaInfoRepo.getMediaEntityDetails(id))
    }.stateIn(
        scope = backgroundScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )
}
