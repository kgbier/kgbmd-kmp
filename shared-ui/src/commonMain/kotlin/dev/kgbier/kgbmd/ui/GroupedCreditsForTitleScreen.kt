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
import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleListUiState
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleListItem
import dev.kgbier.kgbmd.presentation.GroupedCreditsForTitleViewModel
import dev.kgbier.kgbmd.ui.component.CastCreditRow
import dev.kgbier.kgbmd.ui.component.GroupingHeaderRow
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun GroupedCreditsForTitleScreen(
    id: MediaEntityId,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
    viewModelFactory: GroupedCreditsForTitleViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: GroupedCreditsForTitleViewModel = remember(id) {
        viewModelFactory.createGroupedCreditsForTitleViewModel(scope, id)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val expandedGroups by viewModel.expandedGroups.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.load()
    }

    when (val state = state) {
        is GroupedCreditsForTitleListUiState.Loading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            LoadingIndicator()
        }

        is GroupedCreditsForTitleListUiState.Error -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            Text("Failed to load credits")
        }

        is GroupedCreditsForTitleListUiState.Items -> GroupedCreditsForTitleList(
            state = state,
            expandedGroups = expandedGroups,
            onGroupClick = { viewModel.toggleGroupExpanded(it.id) },
            onCreditRowClick = { router.push(AppRoute.Details(it.nameProfile.id)) },
            contentPadding = contentPadding,
        )
    }
}

@Composable
fun GroupedCreditsForTitleList(
    state: GroupedCreditsForTitleListUiState.Items,
    expandedGroups: Set<CreditGroupingId>,
    onCreditRowClick: (CastCredit) -> Unit = {},
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
                is GroupedCreditsForTitleListItem.Grouping -> stickyHeader(
                    key = item.grouping.id.id,
                    contentType = GroupedCreditsForTitleListItem.Grouping::class
                ) {
                    GroupingHeaderRow(
                        isExpanded = isExpanded,
                        onClick = { onGroupClick(item.grouping) },
                        text = item.grouping.name,
                        label = item.grouping.count.toString(),
                    )
                }

                is GroupedCreditsForTitleListItem.Credit -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:${item.credit.nameProfile.id.id}",
                        contentType = GroupedCreditsForTitleListItem.Credit::class,
                    ) {
                        CastCreditRow(
                            name = item.credit.nameProfile.name,
                            roles = item.credit.roles,
                            creditImageUrl = item.credit.nameProfile.photo?.thumbnailUrl,
                            onClick = { onCreditRowClick(item.credit) },
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 6.dp,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                is GroupedCreditsForTitleListItem.Loading -> if (isExpanded) {
                    item(
                        key = "${item.groupingId.id}:loading",
                        contentType = GroupedCreditsForTitleListItem.Loading::class,
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
