package dev.kgbier.kgbmd.ui.nav

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene

@OptIn(ExperimentalMaterial3Api::class)
internal data class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    lateinit var sheetState: SheetState

    override val content: @Composable (() -> Unit) = {
        sheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
        ModalBottomSheet(
            onDismissRequest = onBack,
            properties = modalBottomSheetProperties,
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle(modifier = Modifier.padding(horizontal = 48.dp)) },
            // We want to draw ModalBottomSheet content edge-to-edge, so the bottom insets must be handled internally
            contentWindowInsets = { WindowInsets.safeDrawing.only(WindowInsetsSides.Top) },
        ) {
            entry.Content()
        }
    }

    override suspend fun onRemove() {
        // run hide animations when this bottom sheet is popped from the backStack
        sheetState.hide()
    }
}
