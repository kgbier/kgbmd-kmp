package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.kgbier.kgbmd.di.CoreModule
import dev.kgbier.kgbmd.di.KtorModule
import dev.kgbier.kgbmd.di.RepoModule
import dev.kgbier.kgbmd.di.ServiceModule
import dev.kgbier.kgbmd.di.StorageModule
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameViewModel
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleViewModel
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.presentation.SearchScreenViewModel
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
    val repoModule = RepoModule(serviceModule)
    val viewModelModule = ViewModelModule(this)
}

val LocalViewModelModule =
    staticCompositionLocalOf<ViewModelModule> { error("ViewModelModule must be provided") }

class ViewModelModule(
    val rootModule: RootModule,
) :
    TitleListViewModel.Factory,
    MainScreenViewModel.Factory,
    DetailsScreenViewModel.Factory,
    GroupedCreditsForTitleViewModel.Factory,
    GroupedCreditsForNameViewModel.Factory,
    SearchScreenViewModel.Factory {

    override fun createTitleListViewModelFactory(
        scope: CoroutineScope,
    ): TitleListViewModel = TitleListViewModel(
        scope = scope,
        preferencesRepo = rootModule.storageModule.preferencesRepo,
        mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
    )

    override fun createMainScreenViewModelFactory(
        scope: CoroutineScope,
    ): MainScreenViewModel = MainScreenViewModel(
        scope = scope,
        preferencesRepo = rootModule.storageModule.preferencesRepo
    )

    override fun createSearchScreenViewModelFactory(
        scope: CoroutineScope,
    ): SearchScreenViewModel = SearchScreenViewModel(
        scope = scope,
        mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
    )

    override fun createDetailsScreenViewModelFactory(
        scope: CoroutineScope,
        id: MediaEntityId,
    ): DetailsScreenViewModel = DetailsScreenViewModel(
        scope = scope,
        id = id,
        mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
    )

    override fun createGroupedCreditsForTitleViewModel(
        scope: CoroutineScope,
        id: MediaEntityId
    ): GroupedCreditsForTitleViewModel = GroupedCreditsForTitleViewModel(
        scope = scope,
        id = id,
        mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
    )

    override fun createGroupedCreditsForNameViewModel(
        scope: CoroutineScope,
        id: MediaEntityId
    ): GroupedCreditsForNameViewModel = GroupedCreditsForNameViewModel(
        scope = scope,
        id = id,
        mediaInfoRepo = rootModule.repoModule.mediaInfoRepo,
    )
}
