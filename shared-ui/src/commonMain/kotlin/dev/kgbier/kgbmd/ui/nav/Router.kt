package dev.kgbier.kgbmd.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

interface Route

interface Navigator<TRoute : Route> {
    fun push(route: TRoute)
    fun dismiss()
    fun pop()
}

interface Router<TRoute : Route> {
    val navigator: Navigator<TRoute>

    val currentRoute: TRoute
    val hasBackstack: Boolean
}

fun <TRoute : Route> Router(
    initialRoute: TRoute,
    parentRouter: Router<*>? = null,
): Router<TRoute> = StackRouter(initialRoute = initialRoute, parentRouter = parentRouter)

@Composable
fun <TRoute : Route> rememberRouter(
    initialRoute: TRoute,
    parentRouter: Router<*>? = null,
): Router<TRoute> = remember(initialRoute, parentRouter) {
    Router(initialRoute, parentRouter)
}

class StackRouter<TRoute : Route>(
    initialRoute: TRoute,
    private val parentRouter: Router<*>? = null,
) : Router<TRoute> {

    override val navigator: Navigator<TRoute> = StackNavigator()

    private val backstack = mutableStateListOf(initialRoute)

    override val currentRoute: TRoute get() = backstack.last()
    override val hasBackstack: Boolean get() = backstack.size > 1

    private inner class StackNavigator : Navigator<TRoute> {
        override fun push(route: TRoute) {
            backstack.add(route)
        }

        override fun pop() {
            backstack.removeLast()
        }

        override fun dismiss() {
            parentRouter?.navigator?.pop()
        }
    }
}
