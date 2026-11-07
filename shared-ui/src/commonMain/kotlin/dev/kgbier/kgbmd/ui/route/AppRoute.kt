package dev.kgbier.kgbmd.ui.route

import dev.kgbier.kgbmd.domain.model.Image
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.ui.nav.Route

/**
 * [BottomsheetRoute] indicates a route to be presented as a bottomsheet via [dev.kgbier.kgbmd.ui.nav.BottomSheetSceneStrategy].
 * There is a gap in behaviour when [androidx.compose.material3.ModalBottomSheet] is used in an [androidx.navigation3.scene.OverlayScene]:
 * - https://github.com/android/nav3-recipes/issues/154
 * - https://github.com/android/nav3-recipes/issues/160
 * - https://issuetracker.google.com/issues/318537200
 *
 * This is mitigated in the router by removing the [BottomsheetRoute] from the backstack when navigation occurs forward from it.
 */
interface BottomsheetRoute : Route

sealed interface AppRoute : Route {
    object Main : AppRoute
    object Search : AppRoute
    data class Details(val id: MediaEntityId) : AppRoute
    object Settings : AppRoute
    data class ImageViewer(val image: Image) : AppRoute
    data class CastAndCrew(val id: MediaEntityId) : AppRoute, BottomsheetRoute
    data class Filmography(val id: MediaEntityId) : AppRoute, BottomsheetRoute
    data class Episodes(val id: MediaEntityId) : AppRoute, BottomsheetRoute
}
