package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.withCompositionLocals
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.di.RootModule
import dev.kgbier.kgbmd.ui.nav.Nav
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.theme.AppTheme

@Composable
fun MainRoot() = AppTheme {

    // Coil
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    val di = remember { RootModule() }

    BoxWithConstraints {

        withCompositionLocals(
            LocalSizeClass provides computeSizeClass(),
            LocalViewModelModule provides di.viewModelModule,
        ) {

            Nav<AppRoute>(initialRoute = AppRoute.Main) { router ->
                when (val route = router.currentRoute) {
                    AppRoute.Main -> MainScreen(router.navigator)
                    AppRoute.Search -> SearchScreen(router.navigator)
                    is AppRoute.Details -> DetailsScreen(id = route.id)
                    AppRoute.Settings -> SettingsScreen()
                }
            }
        }
    }
}
