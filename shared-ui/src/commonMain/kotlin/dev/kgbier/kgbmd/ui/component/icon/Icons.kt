package dev.kgbier.kgbmd.ui.component.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * https://fonts.google.com/icons
 */
object Icons {
    val ArrowBack get() = arrow_back

    val ChevronRight get() = chevron_right
    val Close get() = close
    val ConfirmationNumber get() = confirmation_number

    val Error get() = error

    val FitScreen get() = fit_screen

    val Movie get() = movie

    val Person get() = person

    val Search get() = search
    val Schedule get() = schedule

    val Tune get() = tune
    val Tv get() = tv
}

@Composable
internal fun IconPreview(imageVector: ImageVector) {
    Icon(imageVector = imageVector, contentDescription = null)
}
