package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.withCompositionLocals
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.kgbier.kgbmd.ui.di.LifecycleViewModelModule
import dev.kgbier.kgbmd.ui.di.LocalRootModule
import dev.kgbier.kgbmd.ui.di.rememberRootModule
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.nav.registerLocalNavigationEventDispatcher
import dev.kgbier.kgbmd.ui.nav.rememberNavigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.theme.AppTheme
import dev.kgbier.kgbmd.ui.util.LocalSizeClass
import dev.kgbier.kgbmd.ui.util.computeSizeClass

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
        set(LifecycleViewModelModule.Key.rootModule, LocalRootModule.current)
        extrasBlock()
    }
    return viewModel(VM::class, viewModelStoreOwner, key, LifecycleViewModelModule.factory, extras)
}

@Composable
fun MainRoot() = AppTheme {

    // Coil
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    val di = rememberRootModule()

    BoxWithConstraints {

        val navigator: Navigator<AppRoute> = rememberNavigator(AppRoute.Main)
        registerLocalNavigationEventDispatcher()

        withCompositionLocals(
            LocalSizeClass provides computeSizeClass(),
            LocalRootModule provides di,
        ) {
            NavDisplay(
                backStack = navigator.backstack,
                onBack = { navigator.router.pop() },
                entryDecorators = listOf(
                    // Add the default decorators for managing scenes and saving state
                    rememberSaveableStateHolderNavEntryDecorator(),
                    // Then add the view model store decorator
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            ) { key ->
                when (key) {
                    AppRoute.Main -> NavEntry(key) {
                        MainScreen(navigator.router)
                    }

                    AppRoute.Search -> NavEntry(key) {
                        SearchScreen(navigator.router)
                    }

                    is AppRoute.Details -> NavEntry(key) {
                        DetailsScreen(
                            id = key.id,
                            router = navigator.router
                        )
                    }

                    AppRoute.Settings -> NavEntry(key) {
                        SettingsScreen()
                    }

                    is AppRoute.ImageViewer -> NavEntry(key) {
                        ImageScreen(image = key.image)
                    }
                }
            }
        }
    }
}
