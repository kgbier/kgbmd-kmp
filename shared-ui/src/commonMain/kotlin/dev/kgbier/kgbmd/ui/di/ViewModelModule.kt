package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleViewModel
import dev.kgbier.kgbmd.presentation.GroupedEpisodesForTitleViewModel
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.presentation.SearchScreenViewModel
import dev.kgbier.kgbmd.presentation.SettingsScreenViewModel
import dev.kgbier.kgbmd.presentation.TitleListViewModel

@Composable
inline fun <reified VM : ViewModel> appViewModel(
    viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        },
    key: String? = null,
    extrasBlock: MutableCreationExtras.() -> Unit = {},
): VM {
    val extras = MutableCreationExtras().apply {
        set(ViewModelModule.Key.rootModule, LocalRootModule.current)
        extrasBlock()
    }
    return viewModel(VM::class, viewModelStoreOwner, key, ViewModelModule.factory, extras)
}

object ViewModelModule {

    object Key {
        val rootModule = CreationExtras.Key<RootModule>()

        val mediaEntityId = CreationExtras.Key<MediaEntityId>()
    }

    context(extras: CreationExtras)
    private val rootModule: RootModule
        get() = requireNotNull(extras[Key.rootModule]) { "Key `rootModule` in `CreationExtras` is missing." }

    context(extras: CreationExtras)
    private val mediaEntityId: MediaEntityId
        get() = requireNotNull(extras[Key.mediaEntityId]) { "Key `mediaEntityId` in `CreationExtras` is missing." }

    val factory = viewModelFactory {
        initializer {
            MainScreenViewModel(
                preferencesRepo = rootModule.storageModule.preferencesRepo
            )
        }

        initializer {
            SearchScreenViewModel(
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            TitleListViewModel(
                preferencesRepo = rootModule.storageModule.preferencesRepo,
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            DetailsScreenViewModel(
                id = mediaEntityId,
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            GroupedCreditsForTitleViewModel(
                id = mediaEntityId,
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            GroupedCreditsForNameViewModel(
                id = mediaEntityId,
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            GroupedEpisodesForTitleViewModel(
                id = mediaEntityId,
                mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
            )
        }

        initializer {
            SettingsScreenViewModel(
                preferencesRepo = rootModule.storageModule.preferencesRepo,
            )
        }
    }
}
