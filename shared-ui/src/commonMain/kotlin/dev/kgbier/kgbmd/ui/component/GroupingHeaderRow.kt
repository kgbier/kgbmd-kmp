package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.atom.Tag
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.preview.AppPreview

@Composable
fun GroupingHeaderRow(
    isExpanded: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: () -> Unit = {},
) = GroupingHeaderRow(
    isExpanded = isExpanded,
    onClick = onClick,
    trailingContent = label?.let { label ->
        { Tag { Text(label) } }
    },
    modifier = modifier,
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun GroupingHeaderRow(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    trailingContent: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "ChevronRotation"
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            content()
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (trailingContent != null) {
                    Spacer(Modifier.width(8.dp))
                    trailingContent()
                }
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        }
    }
}

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreview() {
    GroupingHeaderRow(
        isExpanded = false,
        text = "Title",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreviewLabel() {
    GroupingHeaderRow(
        isExpanded = false,
        text = "Title",
        label = "Label",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreviewExpanded() {
    GroupingHeaderRow(
        isExpanded = true,
        text = "Title",
        modifier = Modifier.fillMaxWidth()
    )
}
