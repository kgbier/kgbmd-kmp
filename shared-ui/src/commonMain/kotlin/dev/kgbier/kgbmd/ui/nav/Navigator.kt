package dev.kgbier.kgbmd.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

interface Route

interface Router<TRoute : Route> {
    fun push(route: TRoute)
    fun dismiss()
    fun pop()
}

interface Navigator<TRoute : Route> {
    val router: Router<TRoute>

    val currentRoute: TRoute
    val previousRoute: TRoute?

    val backstackSize: Int
    val hasBackstack: Boolean
}

fun <TRoute : Route> Navigator(
    initialRoute: TRoute,
    parentNavigator: Navigator<*>? = null,
): Navigator<TRoute> =
    StackNavigator(initialRoute = initialRoute, parentNavigator = parentNavigator)

@Composable
fun <TRoute : Route> rememberNavigator(
    initialRoute: TRoute,
    parentNavigator: Navigator<*>? = null,
): Navigator<TRoute> = remember(initialRoute, parentNavigator) {
    Navigator(initialRoute, parentNavigator)
}

class StackNavigator<TRoute : Route>(
    initialRoute: TRoute,
    private val parentNavigator: Navigator<*>? = null,
) : Navigator<TRoute> {

    override val router: Router<TRoute> = StackRouter()

    private val backstack = mutableStateListOf(initialRoute)

    override val currentRoute: TRoute get() = backstack.last()
    override val previousRoute: TRoute? get() = backstack.getOrNull(backstack.lastIndex - 1)

    override val backstackSize: Int get() = backstack.size
    override val hasBackstack: Boolean get() = backstack.size > 1

    private inner class StackRouter : Router<TRoute> {
        override fun push(route: TRoute) {
            backstack.add(route)
        }

        override fun pop() {
            backstack.removeLast()
        }

        override fun dismiss() {
            parentNavigator?.router?.pop()
        }
    }
}
