package dev.kgbier.kgbmd.ui.nav

import androidx.compose.animation.core.SeekableTransitionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

actual fun <TRoute : Route> onBackCancelledHandler(
    scope: CoroutineScope,
    seekableTransitionState: SeekableTransitionState<TRoute>,
    router: Router<TRoute>
) {
    scope.launch { seekableTransitionState.animateTo(router.currentRoute) }
}
