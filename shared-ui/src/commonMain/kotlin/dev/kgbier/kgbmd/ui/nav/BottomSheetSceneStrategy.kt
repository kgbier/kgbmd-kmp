package dev.kgbier.kgbmd.ui.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/**
 * A [androidx.navigation3.scene.SceneStrategy] that displays entries that have added [bottomSheet] to their [androidx.navigation3.runtime.NavEntry.metadata]
 * within a [androidx.compose.material3.ModalBottomSheet] instance.
 *
 * This strategy should always be added before any non-overlay scene strategies.
 */
@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val bottomSheetProperties = lastEntry.metadata[BottomSheetKey] ?: return null

        @Suppress("UNCHECKED_CAST")
        return BottomSheetScene(
            key = lastEntry.contentKey as T,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            entry = lastEntry,
            modalBottomSheetProperties = bottomSheetProperties,
            onBack = onBack,
        )
    }

    companion object {
        /**
         * Function to be called on the [NavEntry.metadata] to mark this entry as something that
         * should be displayed within a [androidx.compose.material3.ModalBottomSheet].
         *
         * @param modalBottomSheetProperties properties that should be passed to the containing
         * [androidx.compose.material3.ModalBottomSheet].
         */
        fun bottomSheet(
            modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ) = metadata {
            put(BottomSheetKey, modalBottomSheetProperties)
        }

        object BottomSheetKey : NavMetadataKey<ModalBottomSheetProperties>
    }
}
