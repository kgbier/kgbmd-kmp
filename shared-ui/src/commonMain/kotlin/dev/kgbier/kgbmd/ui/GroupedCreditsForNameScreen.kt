@file:OptIn(ExperimentalFoundationApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleCredit
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameListItem
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameListUiState
import dev.kgbier.kgbmd.presentation.GroupedCreditsForNameViewModel
import dev.kgbier.kgbmd.ui.component.GroupingHeaderRow
import dev.kgbier.kgbmd.ui.component.TitleCreditRow
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun GroupedCreditsForNameScreen(
    id: MediaEntityId,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
    viewModelFactory: GroupedCreditsForNameViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: GroupedCreditsForNameViewModel = remember(id) {
        viewModelFactory.createGroupedCreditsForNameViewModel(scope, id)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val expandedGroups by viewModel.expandedGroups.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.load()
    }

    when (val state = state) {
        is GroupedCreditsForNameListUiState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            LoadingIndicator()
        }

        is GroupedCreditsForNameListUiState.Error -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            Text("Failed to load credits")
        }

        is GroupedCreditsForNameListUiState.Items -> GroupedCreditsForNameList(
            state = state,
            expandedGroups = expandedGroups,
            onGroupClick = { viewModel.toggleGroupExpanded(it.id) },
            onCreditRowClick = { router.push(AppRoute.Details(it.moviePoster.id)) },
            contentPadding = contentPadding,
        )
    }
}

@Composable
fun GroupedCreditsForNameList(
    state: GroupedCreditsForNameListUiState.Items,
    expandedGroups: Set<CreditGroupingId>,
    onCreditRowClick: (TitleCredit) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
    onGroupClick: (CreditGrouping) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        state.items.forEach { item ->
            val isExpanded = expandedGroups.contains(item.groupingId)

            when (item) {
                is GroupedCreditsForNameListItem.Grouping -> stickyHeader(
                    key = item.grouping.id.id,
                    contentType = GroupedCreditsForNameListItem.Grouping::class
                ) {
                    GroupingHeaderRow(
                        isExpanded = isExpanded,
                        onClick = { onGroupClick(item.grouping) },
                        text = item.grouping.name,
                        label = item.grouping.count.toString(),
                    )
                }

                is GroupedCreditsForNameListItem.Credit -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:${item.credit.moviePoster.id.id}",
                        contentType = GroupedCreditsForNameListItem.Credit::class,
                    ) {
                        TitleCreditRow(
                            title = item.credit.moviePoster.title,
                            rating = item.credit.moviePoster.rating,
                            posterImageUrl = item.credit.moviePoster.thumbnailUrl,
                            year = item.credit.year,
                            status = item.credit.productionStatus,
                            roles = item.credit.roles,
                            onClick = { onCreditRowClick(item.credit) },
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 6.dp,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is GroupedCreditsForNameListItem.Loading -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:loading",
                        contentType = GroupedCreditsForNameListItem.Loading::class,
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
