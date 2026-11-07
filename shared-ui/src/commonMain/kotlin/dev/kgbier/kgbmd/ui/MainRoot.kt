package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.withCompositionLocals
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import coil3.compose.setSingletonImageLoaderFactory
import dev.kgbier.kgbmd.ui.di.CoilModule
import dev.kgbier.kgbmd.ui.di.LocalRootModule
import dev.kgbier.kgbmd.ui.di.rememberRootModule
import dev.kgbier.kgbmd.ui.nav.BottomSheetSceneStrategy
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.nav.registerLocalNavigationEventDispatcher
import dev.kgbier.kgbmd.ui.nav.rememberNavigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.theme.AppTheme
import dev.kgbier.kgbmd.ui.util.LocalSizeClass
import dev.kgbier.kgbmd.ui.util.computeSizeClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoot() = AppTheme {

    // Coil
    setSingletonImageLoaderFactory { context ->
        CoilModule.createImageLoader(context)
    }

    val di = rememberRootModule()
    val navigator: Navigator<AppRoute> = rememberNavigator(AppRoute.Main)
    registerLocalNavigationEventDispatcher()

    BoxWithConstraints {
        withCompositionLocals(
            LocalSizeClass provides computeSizeClass(),
            LocalRootModule provides di,
        ) {
            NavDisplay(
                backStack = navigator.backstack,
                onBack = { navigator.router.pop() },
                sceneStrategies = listOf(
                    remember { BottomSheetSceneStrategy() },
                ),
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

                    is AppRoute.CastAndCrew -> NavEntry(
                        key = key,
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        GroupedCreditsForTitleScreen(
                            id = key.id,
                            router = navigator.router,
                            contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                                .asPaddingValues(),
                        )
                    }

                    is AppRoute.Filmography -> NavEntry(
                        key = key,
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        GroupedCreditsForNameScreen(
                            id = key.id,
                            router = navigator.router,
                            contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                                .asPaddingValues(),
                        )
                    }

                    is AppRoute.Episodes -> NavEntry(
                        key = key,
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        GroupedEpisodesForTitleScreen(
                            id = key.id,
                            router = navigator.router,
                            contentPadding = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                                .asPaddingValues(),
                        )
                    }
                }
            }
        }
    }
}
