@file:OptIn(ExperimentalComposeUiApi::class)

package dev.kgbier.kgbmd.ui.nav

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
    routeHandler: @Composable (route: TRoute, navigator: Navigator<TRoute>) -> Unit,
) {
    val navState = rememberNavigationEventState(NavigationEventInfo.None)

    val router: Router<TRoute> = rememberRouter(
        initialRoute = initialRoute,
        parentRouter = parentRouter,
    )

    val owner = LocalNavigationEventDispatcherOwner.current
    val input = LocalDirectNavigationEventInput.current

    DisposableEffect(owner) {
        owner?.navigationEventDispatcher?.addInput(input)
        onDispose {
            owner?.navigationEventDispatcher?.removeInput(input)
        }
    }



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
    val seekableTransitionState = remember { SeekableTransitionState(router.currentRoute) }
    val transition = rememberTransition(seekableTransitionState, label = "navTransition")
    var predictiveBackEdge by remember { mutableIntStateOf(NavigationEvent.EDGE_NONE) }
    var navigatingBackwards by remember { mutableStateOf(false) }

    LaunchedEffect(router.currentRoute) {
        navigatingBackwards = false
        seekableTransitionState.animateTo(router.currentRoute)
    }

    LaunchedEffect(navState.transitionState) {
        val transitionState = navState.transitionState
        val previousRoute = router.previousRoute ?: return@LaunchedEffect

        if (transitionState is NavigationEventTransitionState.InProgress) {
            // Predictive Back is happening
            navigatingBackwards = true
            predictiveBackEdge = transitionState.latestEvent.swipeEdge
            val progress = transitionState.latestEvent.progress

            seekableTransitionState.seekTo(progress, previousRoute)
        } else {
            navigatingBackwards = false
        }
    }

    transition.AnimatedContent(
        transitionSpec = {
            // Determine if the transition spec will apply to the previous or current content
            val isPreviousContent = targetState == router.previousRoute

            if (isPreviousContent) {
                scaleIn(
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    initialScale = 0.9f,
                ) togetherWith slideOut { fullSize ->
                    val xOffset = if (predictiveBackEdge == NavigationEvent.EDGE_RIGHT) {
                        -fullSize.width
                    } else {
                        fullSize.width
                    }
                    IntOffset(xOffset, 0)
                }
            } else {
                fadeIn() togetherWith ExitTransition.None
            }.apply {
                val backstackIndex = router.backstackSize
                // Previous content should be drawn below the current content
                val adjustedBackstackIndex =
                    if (isPreviousContent) backstackIndex - 1 else backstackIndex
                targetContentZIndex = adjustedBackstackIndex.toFloat()
            }
        },
    ) { route ->
        routeHandler(route, router.navigator)
    }
}