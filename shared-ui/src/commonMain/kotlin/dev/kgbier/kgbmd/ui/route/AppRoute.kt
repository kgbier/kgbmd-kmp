package dev.kgbier.kgbmd.ui.route

import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.ui.nav.Route

sealed interface AppRoute: Route {
    object Main : AppRoute
    object Search : AppRoute
    data class Details(val id: MediaEntityId) : AppRoute
    object Settings : AppRoute
}