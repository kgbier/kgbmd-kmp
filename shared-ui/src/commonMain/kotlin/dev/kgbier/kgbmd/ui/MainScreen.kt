@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kgbier.kgbmd.domain.model.TitleCategory
import dev.kgbier.kgbmd.presentation.MainScreenViewModel
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreen(
    router: Router<AppRoute>,
    viewModelFactory: MainScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: MainScreenViewModel = remember {
        viewModelFactory.createMainScreenViewModelFactory(scope)
    }
) {
    val titleCategory by viewModel.titleCategory.collectAsStateWithLifecycle(TitleCategory.Movie)

    MainScreen(
        currentCategory = titleCategory,
        onTitleCategoryIconClicked = viewModel::toggleTitleCategory,
        onSearchIconClicked = { router.push(AppRoute.Search) },
        onSettingsIconClicked = { router.push(AppRoute.Settings) },
    ) { innerPadding ->
        TitleList(
            router = router,
            scope = scope,
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun MainScreen(
    currentCategory: TitleCategory,
    onTitleCategoryIconClicked: () -> Unit = {},
    onSearchIconClicked: () -> Unit = {},
    onSettingsIconClicked: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {
    val scrollBehaviour = FloatingToolbarDefaults.exitAlwaysScrollBehavior(
        exitDirection = FloatingToolbarExitDirection.Bottom,
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = Modifier
            .nestedScroll(scrollBehaviour)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
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
                IconButton(onClick = onTitleCategoryIconClicked) {
                    val toggleCategoryIcon = when (currentCategory) {
                        TitleCategory.Movie -> Icons.Tv
                        TitleCategory.TvShow -> Icons.Movie
                    }
                    Icon(
                        toggleCategoryIcon,
                        contentDescription = "Localized description",
                    )
                }
                FilledIconButton(
                    onClick = onSearchIconClicked,
                    modifier = Modifier
                        .width(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide).width),
                ) {
                    Icon(
                        Icons.Search,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = onSettingsIconClicked) {
                    Icon(
                        Icons.Tune,
                        contentDescription = "Localized description",
                    )
                }
            }
            content(innerPadding)
        }
    }
}

@Composable
@AppPreview.Screen
fun MainScreenPreview() {
    MainScreen(
        currentCategory = TitleCategory.Movie,
    )
}
