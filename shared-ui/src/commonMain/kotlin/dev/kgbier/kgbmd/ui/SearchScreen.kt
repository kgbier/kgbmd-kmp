@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.presentation.SearchScreenViewModel
import dev.kgbier.kgbmd.ui.component.SearchSuggestion
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.di.appViewModel
import dev.kgbier.kgbmd.ui.nav.LocalDirectNavigationEventInput
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.ScrollToTopHandler

@Composable
fun SearchScreen(
    router: Router<AppRoute>,
    viewModel: SearchScreenViewModel = appViewModel(),
) {
    val state by viewModel.viewState.collectAsState()
    SearchScreen(
        state = state,
        onSearchQueryUpdated = viewModel::search,
        onSearchItemClicked = { router.push(AppRoute.Details(it)) },
    )
}

@Composable
fun SearchScreen(
    state: SearchScreenViewModel.SearchScreenViewState,
    onSearchQueryUpdated: (String) -> Unit = {},
    onSearchItemClicked: (MediaEntityId) -> Unit = {},
) {
    val backEventInput = LocalDirectNavigationEventInput.current

    Scaffold { innerPadding ->
        val textFieldState = rememberTextFieldState()

        LaunchedEffect(textFieldState) {
            snapshotFlow { textFieldState.text }
                .collect { onSearchQueryUpdated(it.toString()) }
        }

        val focusRequester = remember { FocusRequester() }

        // Focus the input field on the first expansion,
        // but no need to re-focus if the focus gets cleared.
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        Column(
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            Surface(
                color = SearchBarDefaults.colors().containerColor,
                contentColor = contentColorFor(SearchBarDefaults.colors().containerColor),
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
            ) {
                SearchBarDefaults.InputField(
                    state = textFieldState,
                    onSearch = { },
                    expanded = true,
                    onExpandedChange = {},
                    placeholder = { Text(text = "Search movies, shows, actors") },
                    leadingIcon = {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = { PlainTooltip { Text("Back") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(
                                onClick = { backEventInput.backCompleted() }
                            ) {
                                Icon(
                                    Icons.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }

                    },
                    trailingIcon = {
                        if (textFieldState.text.isEmpty()) return@InputField

                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(
                                    TooltipAnchorPosition.Above
                                ),
                            tooltip = { PlainTooltip { Text("Close") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = { textFieldState.clearText() }) {
                                Icon(
                                    imageVector = Icons.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(top = innerPadding.calculateTopPadding())
                )
            }
            if (state is SearchScreenViewModel.SearchScreenViewState.Loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateBottomPadding()
                        )
                ) {
                    LoadingIndicator()
                }
            }

            val scrollState = rememberScrollState()
            ScrollToTopHandler(scrollState)

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(
                        bottom = WindowInsets.safeDrawing
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {
                when (val state = state) {
                    SearchScreenViewModel.SearchScreenViewState.Idle -> Unit
                    SearchScreenViewModel.SearchScreenViewState.Loading -> Unit
                    is SearchScreenViewModel.SearchScreenViewState.Items -> {
                        for (item in state.items) {
                            SearchSuggestion(
                                suggestion = item.suggestion,
                                ratingState = item.ratingState,
                                modifier = Modifier
                                    .clickable(
                                        onClick = { onSearchItemClicked(item.suggestion.id) }
                                    ).padding(16.dp, 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@AppPreview.Screen
fun SearchScreenPreview() {
    SearchScreen(
        state = SearchScreenViewModel.SearchScreenViewState.Idle,
    )
}
