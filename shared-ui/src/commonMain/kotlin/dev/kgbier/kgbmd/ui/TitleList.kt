package dev.kgbier.kgbmd.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.TitleListViewModel
import dev.kgbier.kgbmd.ui.component.ErrorPanel
import dev.kgbier.kgbmd.ui.component.PosterView
import dev.kgbier.kgbmd.ui.component.atom.PosterCard
import dev.kgbier.kgbmd.ui.component.atom.ShimmerEffect
import dev.kgbier.kgbmd.ui.component.atom.rememberShimmerState
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.component.preview.sampleMoviePoster
import dev.kgbier.kgbmd.ui.component.toErrorMessage
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.LocalSizeClass
import dev.kgbier.kgbmd.ui.util.ScrollToTopHandler
import dev.kgbier.kgbmd.ui.util.SizeClass
import kotlinx.coroutines.CoroutineScope

/**
 * A scrollable grid of movie posters.
 */
@Composable
fun TitleList(
    router: Router<AppRoute>,
    viewModelFactory: TitleListViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: TitleListViewModel = remember {
        viewModelFactory.createTitleListViewModelFactory(scope)
    }
) {
    val state: TitleListViewModel.TitleListState by viewModel.titleList.collectAsState()
    TitleListView(
        state = state,
        onTitleItemClicked = { router.push(AppRoute.Details(it)) },
        onRetry = { viewModel.retry() },
        contentPadding = contentPadding,
    )
}

@Composable
fun TitleListView(
    state: TitleListViewModel.TitleListState,
    onTitleItemClicked: (MediaEntityId) -> Unit = {},
    onRetry: () -> Unit = {},
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
        is TitleListViewModel.TitleListState.Loaded,
        is TitleListViewModel.TitleListState.Error,
            -> 0f
    }

    val minPosterSize = when (LocalSizeClass.current) {
        SizeClass.Compact -> 100.dp
        SizeClass.Medium -> 120.dp
        SizeClass.Expanded -> 160.dp
    }

    val lazyGridState = rememberLazyGridState()
    ScrollToTopHandler(lazyGridState)

    if (state is TitleListViewModel.TitleListState.Error) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ErrorPanel(
                errorMessage = state.error.toErrorMessage(),
                onPrimaryActionClick = onRetry,
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = minPosterSize),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyGridState,
            contentPadding = composedContentPadding,
        ) {
            when (state) {
                is TitleListViewModel.TitleListState.Error -> Unit // The grid will not show

                is TitleListViewModel.TitleListState.Loaded -> items(items = state.items) { item ->
                    PosterView(
                        poster = item,
                        onClick = { onTitleItemClicked(item.id) },
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
}

@Composable
@AppPreview.Screen
fun TitleListViewPreview() {
    TitleListView(
        onTitleItemClicked = {},
        onRetry = {},
        state = TitleListViewModel.TitleListState.Loaded(
            items = listOf(
                sampleMoviePoster,
                sampleMoviePoster,
                sampleMoviePoster,
                sampleMoviePoster,
                sampleMoviePoster,
            )
        ),
    )
}

@Composable
@AppPreview.Screen
fun TitleListErrorPreview() {
    TitleListView(
        onTitleItemClicked = {},
        onRetry = {},
        state = TitleListViewModel.TitleListState.Error(
            error = IllegalArgumentException("Failure")
        ),
    )
}
