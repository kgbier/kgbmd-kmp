package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.Size
import dev.kgbier.kgbmd.domain.model.Image

@Composable
fun rememberImageViewerState() = remember { ImageViewerState() }

@Stable
class ImageViewerState {

    @get:FrequentlyChangingValue
    var scale: Float by mutableFloatStateOf(defaultScale)

    @get:FrequentlyChangingValue
    var offset: Offset by mutableStateOf(defaultOffset)

    val isDirty: Boolean
        get() = offset != defaultOffset || scale != defaultScale

    fun reset() {
        scale = defaultScale
        offset = defaultOffset
    }

    private companion object {
        val defaultScale = 1f
        val defaultOffset = Offset.Zero
    }
}

@Composable
fun ImageViewerContainer(
    image: Image,
    modifier: Modifier = Modifier.Companion,
    state: ImageViewerState = rememberImageViewerState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures { centroid, panDelta, zoomDelta, _ ->
                    val oldScale = state.scale
                    state.scale *= zoomDelta

                    val oldCoordinate = state.offset + centroid / oldScale
                    val targetCoordinate = centroid / state.scale + panDelta / oldScale
                    state.offset = oldCoordinate - targetCoordinate
                }
            }.graphicsLayer {
                translationX = -state.offset.x * state.scale
                translationY = -state.offset.y * state.scale
                scaleX = state.scale
                scaleY = state.scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
    ) {
        AsyncImage(
            contentDescription = null,
            contentScale = ContentScale.Fit,
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(image.originalUrl)
                .size(Size.ORIGINAL)
                .placeholderMemoryCacheKey(image.thumbnailUrl)
                .build(),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        )
    }
}
