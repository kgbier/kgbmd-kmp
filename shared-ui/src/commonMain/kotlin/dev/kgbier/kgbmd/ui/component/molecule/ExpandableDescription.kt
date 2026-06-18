package dev.kgbier.kgbmd.ui.component.molecule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.ExpandableContent
import dev.kgbier.kgbmd.ui.component.rememberExpandableContentState

@Composable
fun ExpandableDescription(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    val state = rememberExpandableContentState(collapsedContentHeight = 192.dp)

    ExpandableContent(
        state = state,
        modifier = modifier,
        overlayContent = {
            if (state.isExpandable == true) {
                AnimatedVisibility(
                    visible = !state.isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.surface,
                                    )
                                )
                            ).height(96.dp)
                            .fillMaxWidth()
                    )
                }

                Text(
                    text = if (state.isExpanded) "Collapse" else "Read more",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(if (state.isExpanded) Alignment.BottomStart else Alignment.BottomEnd)
                )
            }
        },
        content = {
            Column(
                modifier = modifier
                    .clickable(enabled = state.isExpandable == true) {
                        state.isExpanded = !state.isExpanded
                    }.padding(contentPadding)
            ) {
                content()
                // Add some spacing at the bottom when the content is expanded to
                // prevent the Expand/Collapse button from overlapping the content
                if (state.isExpanded) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        },
    )
}
