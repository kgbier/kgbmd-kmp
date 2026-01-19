package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.presentation.TitleListViewModel
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.ScrollToTopHandler
import kotlinx.coroutines.CoroutineScope

/**
 * A scrollable grid of movie posters.
 */
@Composable
fun TitleList(
    navigator: Navigator<AppRoute>,
    viewModelFactory: TitleListViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: TitleListViewModel = remember {
        viewModelFactory.createTitleListViewModelFactory(scope)
    }
) {
    val state: TitleListViewModel.TitleListState by viewModel.titleList.collectAsState()
    TitleListView(
        navigate = { navigator.push(it) },
        state = state,
        contentPadding = contentPadding,
    )
}

@Composable
fun TitleListView(
    navigate: (AppRoute) -> Unit,
    state: TitleListViewModel.TitleListState,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val composedContentPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection) + 16.dp,
        top = contentPadding.calculateTopPadding() + 16.dp,
        end = contentPadding.calculateEndPadding(layoutDirection) + 16.dp,
        bottom = contentPadding.calculateBottomPadding() + 16.dp,
    )

    val shimmerState = when (state) {
        TitleListViewModel.TitleListState.Loading -> rememberShimmerState()
        is TitleListViewModel.TitleListState.Loaded -> 0f
    }

    val minPosterSize = when (LocalSizeClass.current) {
        SizeClass.Compact -> 100.dp
        SizeClass.Medium -> 120.dp
        SizeClass.Expanded -> 160.dp
    }

    val lazyGridState = rememberLazyGridState()
    ScrollToTopHandler(lazyGridState)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = minPosterSize),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyGridState,
        contentPadding = composedContentPadding,
    ) {
        when (state) {
            is TitleListViewModel.TitleListState.Loaded -> items(items = state.items) { item ->
                PosterView(
                    poster = item,
                    onClick = { navigate(AppRoute.Details(item.ttId)) },
                )
            }

            TitleListViewModel.TitleListState.Loading -> {
                items(count = 20) {
                    PosterCard(
                        onClick = {},
                        modifier = Modifier.fillMaxSize()
                    ) { ShimmerEffect(shimmerState) }
                }
            }
        }
    }
}

@Composable
@Preview
fun TitleListViewPreview() {
    MaterialTheme {
        Box(Modifier.background(Color.White)) {
            TitleListView(
                navigate = {},
                state = TitleListViewModel.TitleListState.Loaded(
                    items = listOf(
                        MoviePoster(
                            ttId = MediaEntityId("id"),
                            title = "Hello Sailor",
                            rating = null,
                            thumbnailUrl = "url",
                            posterUrlLarge = "url",
                            posterUrlSmall = "url",
                        )
                    )
                )
            )
        }
    }
}
