@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.ExpandedWidth
import kotlinx.coroutines.CoroutineScope

@Composable
fun DetailsScreen(
    id: MediaEntityId,
    router: Router<AppRoute>,
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
                when (val state = state) {
                    is TitleDetails -> TitleDetailsScreen(
                        title = state,
                        router = router,
                        contentPadding = WindowInsets.safeDrawing
                            .asPaddingValues(),
                    )

                    is NameDetails -> NameDetailsScreen(
                        name = state,
                        router = router,
                        contentPadding = WindowInsets.safeDrawing
                            .asPaddingValues(),
                    )

                    null -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingIndicator()
                    }
                }
            }
        }
    }
}
