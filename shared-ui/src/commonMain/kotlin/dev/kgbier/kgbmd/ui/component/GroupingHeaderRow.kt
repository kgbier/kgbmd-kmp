package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    label: String? = null,
    onClick: () -> Unit = {},
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "ChevronRotation"
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .alignByBaseline()
                    .weight(1f)
            )
            if (label != null) {
                Spacer(Modifier.width(8.dp))
                Tag(
                    modifier = Modifier.alignByBaseline()
                ) {
                    Text(label)
                }
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

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreview() {
    GroupingHeaderRow(
        isExpanded = false,
        text = "Title",
    )
}

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreviewLabel() {
    GroupingHeaderRow(
        isExpanded = false,
        text = "Title",
        label = "Label",
    )
}

@Composable
@AppPreview.Component
fun GroupingHeaderRowPreviewExpanded() {
    GroupingHeaderRow(
        isExpanded = true,
        text = "Title",
    )
}
