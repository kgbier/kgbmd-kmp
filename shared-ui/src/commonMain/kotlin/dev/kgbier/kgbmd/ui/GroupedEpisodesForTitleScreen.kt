@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleEpisode
import dev.kgbier.kgbmd.domain.model.TitleSeason
import dev.kgbier.kgbmd.domain.model.TitleSeasonId
import dev.kgbier.kgbmd.presentation.GroupedEpisodesForTitleListItem
import dev.kgbier.kgbmd.presentation.GroupedEpisodesForTitleListUiState
import dev.kgbier.kgbmd.presentation.GroupedEpisodesForTitleViewModel
import dev.kgbier.kgbmd.ui.component.GroupingHeaderRow
import dev.kgbier.kgbmd.ui.component.TitleCreditRow
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun GroupedEpisodesForTitleScreen(
    id: MediaEntityId,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
    viewModelFactory: GroupedEpisodesForTitleViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: GroupedEpisodesForTitleViewModel = remember(id) {
        viewModelFactory.createGroupedEpisodesForTitleViewModel(scope, id)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val expandedGroups by viewModel.expandedGroups.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.load()
    }

    when (val state = state) {
        is GroupedEpisodesForTitleListUiState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            LoadingIndicator()
        }

        is GroupedEpisodesForTitleListUiState.Error -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            Text("Failed to load credits")
        }

        is GroupedEpisodesForTitleListUiState.Items -> GroupedEpisodesForTitleList(
            state = state,
            expandedGroups = expandedGroups,
            onGroupClick = { viewModel.toggleGroupExpanded(it.id) },
            onCreditRowClick = { router.push(AppRoute.Details(it.moviePoster.id)) },
            contentPadding = contentPadding,
        )
    }
}

@Composable
fun GroupedEpisodesForTitleList(
    state: GroupedEpisodesForTitleListUiState.Items,
    expandedGroups: Set<TitleSeasonId>,
    onCreditRowClick: (TitleEpisode) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
    onGroupClick: (TitleSeason) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        state.items.forEach { item ->
            val isExpanded = expandedGroups.contains(item.groupingId)

            when (item) {
                is GroupedEpisodesForTitleListItem.Grouping -> stickyHeader(
                    key = item.grouping.id.id,
                    contentType = GroupedEpisodesForTitleListItem.Grouping::class
                ) {
                    GroupingHeaderRow(
                        text = "Season ${item.grouping.name}",
                        isExpanded = isExpanded,
                        onClick = { onGroupClick(item.grouping) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is GroupedEpisodesForTitleListItem.Episode -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:${item.episode.moviePoster.id}",
                        contentType = GroupedEpisodesForTitleListItem.Episode::class,
                    ) {
                        TitleCreditRow(
                            title = item.episode.moviePoster.title,
                            rating = item.episode.moviePoster.rating,
                            posterImageUrl = item.episode.moviePoster.image?.thumbnailUrl,
                            status = item.episode.releaseDate,
                            year = "Episode ${item.episode.episodeNumber}",
                            onClick = { onCreditRowClick(item.episode) },
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 6.dp,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is GroupedEpisodesForTitleListItem.Loading -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:loading",
                        contentType = GroupedEpisodesForTitleListItem.Loading::class,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth().padding(6.dp)
                        ) {
                            LoadingIndicator(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }
    }
}
