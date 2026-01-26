package dev.kgbier.kgbmd.ui.nav

import androidx.compose.animation.core.SeekableTransitionState
import kotlinx.coroutines.CoroutineScope

expect fun <TRoute : Route> onBackCancelledHandler(
    scope: CoroutineScope,
    seekableTransitionState: SeekableTransitionState<TRoute>,
    router: Router<TRoute>
)
