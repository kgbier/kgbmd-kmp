package dev.kgbier.kgbmd.ui.util

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable

@Composable
expect fun ScrollToTopHandler(scrollState: ScrollableState)
