package dev.kgbier.kgbmd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kgbier.kgbmd.domain.model.Country
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class SettingsScreenViewModel(
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    private val backgroundScope = viewModelScope + Dispatchers.Default

    val country = preferencesRepo.subscribeToCountry()
        .stateIn(
            scope = backgroundScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val postcode = preferencesRepo.subscribeToPostcode().stateIn(
        scope = backgroundScope,
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
