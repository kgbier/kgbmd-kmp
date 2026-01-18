@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import dev.kgbier.kgbmd.data.StubPreferencesService
import dev.kgbier.kgbmd.domain.repo.PreferencesRepo
import dev.kgbier.kgbmd.presentation.MovieListViewModelFactory
import dev.kgbier.kgbmd.ui.theme.AppTheme

@Composable
fun MainScreen() {
    AppTheme {
        val coilContext = LocalPlatformContext.current
        setSingletonImageLoaderFactory {
            ImageLoader.Builder(coilContext)
                .crossfade(true)
                .logger(DebugLogger())
                .build()
        }

        val scope = rememberCoroutineScope()

        val scrollBehaviour = FloatingToolbarDefaults.exitAlwaysScrollBehavior(
            exitDirection = FloatingToolbarExitDirection.Bottom,
        )

        scrollBehaviour.state.offset

        Scaffold(
            bottomBar = {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalFloatingToolbar(
                        expanded = false,
                        scrollBehavior = scrollBehaviour,
                        modifier = Modifier
                            .run {
                                val insetPadding = WindowInsets.safeContent.asPaddingValues()
                                padding(
                                    bottom = insetPadding.calculateBottomPadding(),
                                    top = 0.dp,
                                    start = insetPadding.calculateStartPadding(LocalLayoutDirection.current),
                                    end = insetPadding.calculateEndPadding(LocalLayoutDirection.current),
                                )
                            }
                            .padding(16.dp)
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Tv,
                                contentDescription = "Localized description",
                            )
                        }
                        FilledIconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Tune,
                                contentDescription = "Localized description",
                            )
                        }
                    }
                }
            },
            modifier = Modifier.nestedScroll(scrollBehaviour)
        ) { innerPadding ->
            Box(
                modifier = Modifier.consumeWindowInsets(innerPadding),
            ) {
                TitleList(
                    scope = scope,
                    viewModelFactory = MovieListViewModelFactory(
                        PreferencesRepo(
                            StubPreferencesService()
                        )
                    ),
                    contentPadding = innerPadding,
                )
            }
        }
    }
}

