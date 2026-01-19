package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMaxOfOrDefault
import kotlin.math.max
import kotlin.math.min

object LazyHeadingRow {
    data class Heading(
        /**
         * The list item index this heading is intended for
         */
        val index: Int,
        /**
         * The label of this heading
         */
        val name: String,
    )
}

private data class Group(
    /**
     * The list item index this group heading is intended for
     */
    val index: Int,
    /**
     * A null span group is spanless - it covers the rest of the list.
     */
    val span: Int?,
    /**
     * The label of this group heading
     */
    val name: String
)

private data class OffsetPlaceable(
    /**
     * The index of the candidate list item among all list items
     */
    val index: Int,
    /**
     * The visual offset of the candidate list item this
     * placeable is intended to be drawn at
     */
    val offset: Int,
    /**
     * Subcomposed Placeable
     */
    val placeable: Placeable
)

@Composable
fun LazyHeadingRow(
    headings: List<LazyHeadingRow.Heading>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    headingContent: @Composable (String) -> Unit,
    content: LazyListScope.() -> Unit,
) = Column(modifier = modifier) {

    /**
     * Memoise a spanned variant of all headings
     */
    val groups = remember(headings) {
        headings.mapIndexed { ordinal, heading ->
            val nextIndexOrNull = headings.getOrNull(ordinal + 1)?.index
            val span = if (nextIndexOrNull == null) {
                null
            } else {
                nextIndexOrNull - ordinal
            }
            Group(heading.index, span, heading.name)
        }
    }

    /**
     * Subcompose the headings
     */
    SubcomposeLayout(
        modifier = Modifier
            .padding(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
            )
    ) { constraints ->
        val visibleItems = state.layoutInfo.visibleItemsInfo

        val firstVisibleItem = visibleItems.firstOrNull()
        val lastVisibleItem = visibleItems.lastOrNull()
        if (firstVisibleItem == null || lastVisibleItem == null) {
            return@SubcomposeLayout layout(0, 0) {}
        }

        /**
         * Determine which headings are visible, and record which list items are candidates for
         * header-position reference points
         */
        val candidateHeaderItems = groups.mapNotNull { targetGroup ->
            val isSpanless = targetGroup.span == null
            val isTargetGroupCandidate = if (isSpanless) {
                true
            } else {
                val maxStartOverlap = max(targetGroup.index, firstVisibleItem.index)
                val minEndOverlap =
                    min(targetGroup.index + (targetGroup.span - 1), lastVisibleItem.index)

                val doesOverlap = maxStartOverlap <= minEndOverlap

                doesOverlap
            }

            val candidateHeaderOffsetItem = if (isTargetGroupCandidate) {
                visibleItems.firstOrNull { it.index >= targetGroup.index }
            } else null

            candidateHeaderOffsetItem?.let { targetGroup to it }
        }

        /**
         * Measure and places all heading views, recording any relevant heading metadata
         */
        val placeables = candidateHeaderItems.flatMap { (group, candidate) ->
            subcompose(slotId = candidate.index) {
                headingContent(group.name)
            }.map { measurable ->
                OffsetPlaceable(
                    index = candidate.index,
                    offset = candidate.offset,
                    placeable = measurable.measure(constraints),
                )
            }
        }

        // Identify the first heading view to draw
        val firstSlotId = placeables.firstOrNull()?.index

        layout(
            width = constraints.maxWidth,
            height = placeables.fastMaxOfOrDefault(defaultValue = 0) { it.placeable.height },
        ) {
            // Draw all placeables
            placeables.forEach { offsetPlaceable ->
                val firstHeadingOffset = max(0 + offsetPlaceable.offset, 0)

                // Prepare for a nudge offset for the first (leading) heading,
                // so as to avoid colliding with the next heading.
                val isFirstHeader = offsetPlaceable.index == firstSlotId

                // Determine if the first heading overlaps, and compute how much the overlap is
                val thisNudgeValue = if (isFirstHeader) {
                    val firstHeadingWidth = offsetPlaceable.placeable.width
                    val firstHeadingEnd = firstHeadingWidth + firstHeadingOffset
                    val secondHeadingOffset =
                        candidateHeaderItems.getOrNull(1)?.second?.offset ?: Int.MAX_VALUE

                    val headingOverlap = if (firstHeadingEnd > secondHeadingOffset) {
                        firstHeadingEnd - secondHeadingOffset
                    } else 0

                    headingOverlap
                } else 0

                offsetPlaceable.placeable.place(
                    x = firstHeadingOffset - thisNudgeValue,
                    y = 0,
                )
            }
        }
    }

    LazyRow(
        state = state,
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = contentPadding.calculateBottomPadding(),
        ),
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        overscrollEffect = overscrollEffect,
        content = content,
    )
}