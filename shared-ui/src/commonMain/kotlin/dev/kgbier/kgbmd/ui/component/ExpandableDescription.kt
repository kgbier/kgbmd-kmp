package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
/**
 * WIP
 */
fun ExpandableDescription(
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val collapsedHeight = 96.dp
    var contentHeight by remember { mutableStateOf(0.dp) }
    var isExpanded by remember { mutableStateOf(false) }
    val isExpandable = remember(contentHeight) { contentHeight > collapsedHeight }

    val density = LocalDensity.current

    Column(
        modifier = modifier
            .animateContentSize()
            .then(if (isExpandable) Modifier.clickable {
                isExpanded = !isExpanded
            } else Modifier)
            .padding(contentPadding)
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned { contentHeight = with(density) { it.size.height.toDp() } }
                .then(
                    if (isExpandable && !isExpanded) {
                        Modifier.heightIn(max = collapsedHeight)
                    } else Modifier
                )
        ) {
            content()
        }

        if (isExpandable) Text(
            text = if (!isExpanded) "Read more" else "Read less",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 4.dp)
                .align(Alignment.End)
        )
    }
}