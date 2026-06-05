package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.presentation.GroupedCreditListItem
import dev.kgbier.kgbmd.ui.component.atom.Tag

@Composable
fun GroupingHeaderRow(
    item: GroupedCreditListItem.Grouping,
    isExpanded: Boolean,
    onClick: (CreditGrouping) -> Unit = {},
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "ChevronRotation"
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier
            .clickable { onClick(item.grouping) }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = item.grouping.name,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .alignByBaseline()
                    .weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Tag(
                modifier = Modifier.alignByBaseline()
            ) {
                Text(item.grouping.count.toString())
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.rotate(rotationAngle)
            )
        }
    }
}
