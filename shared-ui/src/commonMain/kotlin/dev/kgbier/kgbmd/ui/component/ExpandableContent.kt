package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun rememberExpandableContentState(
    collapsedContentHeight: Dp = 128.dp,
    initiallyExpanded: Boolean = false,
) = remember {
    ExpandableContentState(
        collapsedContentHeight = collapsedContentHeight,
        initiallyExpanded = initiallyExpanded,
    )
}

@Stable
class ExpandableContentState(
    val collapsedContentHeight: Dp = 128.dp,
    initiallyExpanded: Boolean = false,
) {
    var isExpanded: Boolean by mutableStateOf(initiallyExpanded)
    var isExpandable: Boolean? by mutableStateOf(null)
}

@Composable
fun ExpandableContent(
    state: ExpandableContentState = remember { ExpandableContentState() },
    modifier: Modifier = Modifier,
    overlayContent: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit,
) = Box(
    modifier = modifier
        .clipToBounds()
        .animateContentSize(
            spring(
                stiffness = Spring.StiffnessMedium,
                visibilityThreshold = IntSize.VisibilityThreshold,
            )
        )
) {
    ExpandableContentMeasuringLayout(
        state = state,
    ) { content() }

    overlayContent()
}

@Composable
fun ExpandableContentMeasuringLayout(
    state: ExpandableContentState,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    SubcomposeLayout { constraints ->
        val measurables = subcompose(slotId = "content", content)
        check(measurables.count() == 1) { "ExpandableContent only supports a single child" }
        val measurable = measurables.single()

        val placeable = measurable.measure(constraints)

        val collapsedHeightPx = with(density) { state.collapsedContentHeight.roundToPx() }
        state.isExpandable = placeable.height > collapsedHeightPx

        layout(
            width = placeable.width,
            height = when {
                state.isExpanded -> placeable.height
                state.isExpandable == false -> placeable.height
                else -> collapsedHeightPx
            },
        ) {
            placeable.place(0, 0)
        }
    }
}
