package dev.kgbier.kgbmd.presentation

import dev.kgbier.kgbmd.domain.model.Country
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

class SettingsScreenViewModel(
    private val scope: CoroutineScope,
    private val preferencesRepo: PreferencesRepo
) {
    interface Factory {
        fun createSettingsScreenViewModelFactory(scope: CoroutineScope): SettingsScreenViewModel
    }

    private val backgroundScope = CoroutineScope(scope.newCoroutineContext(Dispatchers.Default))

    val country = preferencesRepo.subscribeToCountry()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val postcode = preferencesRepo.subscribeToPostcode().stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )

    fun updateCountry(country: Country?) {
        backgroundScope.launch { preferencesRepo.setSavedCountry(country) }
    }

    fun updatePostcode(postcode: String) {
        backgroundScope.launch {
            preferencesRepo.setSavedPostcode(postcode.takeUnless { it.isBlank() })
        }
    }
}
