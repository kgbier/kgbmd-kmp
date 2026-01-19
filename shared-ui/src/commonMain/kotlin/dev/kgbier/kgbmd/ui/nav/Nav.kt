@file:OptIn(ExperimentalComposeUiApi::class)

package dev.kgbier.kgbmd.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventInput
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

class GenericBackEvent : NavigationEventInput() {
    fun back() {
        dispatchOnBackCompleted()
    }
}

val LocalGenericBackEventInput = staticCompositionLocalOf { GenericBackEvent() }

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
        ?.addInput(LocalGenericBackEventInput.current)

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
    LaunchedEffect(navState.transitionState) {
        val transitionState = navState.transitionState
        if (transitionState is NavigationEventTransitionState.InProgress) {
            val progress = transitionState.latestEvent.progress
            // Animate the back gesture progress
        }
    }

    routeHandler(router)
}
