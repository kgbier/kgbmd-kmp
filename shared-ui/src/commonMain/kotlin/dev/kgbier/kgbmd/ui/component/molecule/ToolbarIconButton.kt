package dev.kgbier.kgbmd.ui.component.molecule

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ToolbarIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    tooltip: String? = null,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { tooltip?.let { PlainTooltip { Text(it) } } },
        state = rememberTooltipState(),
    ) {
        IconButton(
            onClick = onClick,
            content = { Icon(icon, contentDescription = tooltip) },
            modifier = modifier,
        )
    }
}

@Composable
fun ToolbarFilledIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    tooltip: String? = null,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { tooltip?.let { PlainTooltip { Text(it) } } },
        state = rememberTooltipState(),
    ) {
        FilledIconButton(
            onClick = onClick,
            content = { Icon(icon, contentDescription = tooltip) },
            modifier = modifier,
        )
    }
}
