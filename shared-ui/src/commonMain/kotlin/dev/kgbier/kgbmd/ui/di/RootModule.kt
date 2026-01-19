package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.staticCompositionLocalOf
import dev.kgbier.kgbmd.data.StubPreferencesService
import dev.kgbier.kgbmd.di.KtorModule
import dev.kgbier.kgbmd.di.RepoModule
import dev.kgbier.kgbmd.di.ServiceModule
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.presentation.SearchScreenViewModel
import dev.kgbier.kgbmd.presentation.TitleListViewModel
import kotlinx.coroutines.CoroutineScope

class RootModule {
    val storageModule = StorageModule()
    val ktorModule = KtorModule()
    val serviceModule = ServiceModule(ktorModule)
    val repoModule = RepoModule(serviceModule)
    val viewModelModule = ViewModelModule(this)
}

class StorageModule {
    val preferencesRepo: PreferencesRepo by lazy {
        PreferencesRepo(StubPreferencesService())
    }
}

val LocalViewModelModule =
    staticCompositionLocalOf<ViewModelModule> { error("ViewModelModule must be provided") }

class ViewModelModule(
    val rootModule: RootModule,
) :
    TitleListViewModel.Factory,
    MainScreenViewModel.Factory,
    DetailsScreenViewModel.Factory,
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
}
