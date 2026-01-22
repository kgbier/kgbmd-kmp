@file:OptIn(ExperimentalComposeUiApi::class)

package dev.kgbier.kgbmd.ui.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntOffset
import androidx.navigationevent.DirectNavigationEventInput
import androidx.navigationevent.NavigationEvent
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

val LocalDirectNavigationEventInput = staticCompositionLocalOf { DirectNavigationEventInput() }

@Composable
fun <TRoute : Route> Nav(
    initialRoute: TRoute,
    parentRouter: Router<*>? = null,
    routeHandler: @Composable (router: Router<TRoute>) -> Unit,
) {
    val navState = rememberNavigationEventState(NavigationEventInfo.None)

    val router: Router<TRoute> = rememberRouter(
        initialRoute = initialRoute,
        parentRouter = parentRouter,
    )

    LocalNavigationEventDispatcherOwner.current
        ?.navigationEventDispatcher
        ?.addInput(LocalDirectNavigationEventInput.current)

    NavigationBackHandler(
        state = navState,
        isBackEnabled = router.hasBackstack,
        onBackCancelled = {
            // Process the canceled back gesture
        },
        onBackCompleted = {
            router.navigator.pop()
        }
    )

    // Set up the Transition
    val seekableTransitionState = remember { SeekableTransitionState(true) }
    val transition = rememberTransition(seekableTransitionState, label = "navTransition")
    var lastEdge by remember { mutableIntStateOf(NavigationEvent.EDGE_NONE) }

    LaunchedEffect(navState.transitionState) {
        val transitionState = navState.transitionState
        if (transitionState is NavigationEventTransitionState.InProgress) {
            // Predictive Back is happening
            lastEdge = transitionState.latestEvent.swipeEdge
            val progress = transitionState.latestEvent.progress
            seekableTransitionState.seekTo(progress, false)
        } else {
            seekableTransitionState.animateTo(true)
        }
    }

    transition.AnimatedVisibility(
        visible = { it },
        exit = slideOut { fullSize ->
            val xOffset = if (lastEdge == NavigationEvent.EDGE_RIGHT) {
                -fullSize.width
            } else {
                fullSize.width
            }
            IntOffset((xOffset * 0.2f).toInt(), 0)
        }
    ) {
        routeHandler(router)
    }
}
