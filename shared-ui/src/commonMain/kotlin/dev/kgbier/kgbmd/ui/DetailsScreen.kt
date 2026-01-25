@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.ui.component.TitledContent
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.ExpandedWidth
import kotlinx.coroutines.CoroutineScope

@Composable
fun DetailsScreen(
    id: MediaEntityId,
    navigator: Navigator<AppRoute>,
    viewModelFactory: DetailsScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: DetailsScreenViewModel = remember(id) {
        viewModelFactory.createDetailsScreenViewModelFactory(scope, id)
    }
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = ExpandedWidth.dp)
            ) {
                if (state == null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingIndicator()
                    }
                }

                val screenScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(screenScrollState)
                        .padding(
                            bottom = WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateBottomPadding(),
                        )
                ) {
                    when (val state = state) {
                        is TitleDetails -> TitleDetails(state, navigator)

                        is NameDetails -> NameDetails(state, navigator)

                        null -> Unit // Loading
                    }
                }
            }
        }
    }
}

@Composable
private fun NameDetails(
    name: NameDetails,
    navigator: Navigator<AppRoute>,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    NameDetailsHeader(
        name = name,
        modifier = Modifier
            .padding(16.dp)
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    name.description?.let { description ->
        TitledContent(
            title = "Description",
            body = description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    Spacer(
        modifier = Modifier
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )
}

@Composable
private fun TitleDetails(
    title: TitleDetails,
    navigator: Navigator<AppRoute>,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    TitleDetailsHeader(
        title = title,
        modifier = Modifier
            .padding(16.dp)
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    TitlePrincipalCredits(
        title = title,
        navigator = navigator,
    )

    title.description?.let { description ->
        TitledContent(
            title = "Summary",
            body = description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    Text(
        text = "Cast",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
