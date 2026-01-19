@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.FloatingToolbarExitDirection
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreen(
    navigator: Navigator<AppRoute>,
    viewModelFactory: MainScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: MainScreenViewModel = remember {
        viewModelFactory.createMainScreenViewModelFactory(scope)
    }
) {
    val scrollBehaviour = FloatingToolbarDefaults.exitAlwaysScrollBehavior(
        exitDirection = FloatingToolbarExitDirection.Bottom,
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehaviour)
    ) { innerPadding ->
        Box(
            modifier = Modifier.consumeWindowInsets(innerPadding),
        ) {
            HorizontalFloatingToolbar(
                expanded = false,
                scrollBehavior = scrollBehaviour,
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .offset(y = -ScreenOffset),
            ) {
                IconButton(onClick = viewModel::toggleTitleCategory) {
                    val currentCategory by viewModel.titleCategory.collectAsState(TitleCategory.Movie)
                    val toggleCategoryIcon = when (currentCategory) {
                        TitleCategory.Movie -> Icons.Default.Tv
                        TitleCategory.TvShow -> Icons.Default.Movie
                    }
                    Icon(
                        toggleCategoryIcon,
                        contentDescription = "Localized description",
                    )
                }
                FilledIconButton(
                    onClick = { navigator.push(AppRoute.Search) },
                    modifier = Modifier
                        .width(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide).width),
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { navigator.push(AppRoute.Settings) }) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = "Localized description",
                    )
                }
            }

            TitleList(
                navigator = navigator,
                scope = scope,
                contentPadding = innerPadding,
            )
        }
    }
}
