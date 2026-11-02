package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.kgbier.kgbmd.di.CoreModule
import dev.kgbier.kgbmd.di.KtorModule
import dev.kgbier.kgbmd.di.RepoModule
import dev.kgbier.kgbmd.di.ServiceModule
import dev.kgbier.kgbmd.di.StorageModule
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleViewModel
import dev.kgbier.kgbmd.presentation.GroupedEpisodesForTitleViewModel
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.presentation.SearchScreenViewModel
import dev.kgbier.kgbmd.presentation.SettingsScreenViewModel
import dev.kgbier.kgbmd.presentation.TitleListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job

@Composable
expect fun rememberRootModule(): RootModule

fun CoroutineScope.createChild(): CoroutineScope =
    CoroutineScope(coroutineContext + Job(parent = coroutineContext.job))

class RootModule(
    val coreModule: CoreModule,
) {
    val coroutinesModule = coreModule.coroutinesModule
    val storageModule = StorageModule(coreModule)
    val ktorModule = KtorModule()
    val serviceModule = ServiceModule(ktorModule)
    val repoModule = RepoModule(
        serviceModule = serviceModule,
        storageModule = storageModule,
    )
}

val LocalRootModule =
    staticCompositionLocalOf<RootModule> { error("RootModule must be provided") }

object LifecycleViewModelModule {

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
