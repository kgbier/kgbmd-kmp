@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.Image
import dev.kgbier.kgbmd.ui.component.ImageViewerContainer
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.rememberImageViewerState
import dev.kgbier.kgbmd.ui.nav.LocalDirectNavigationEventInput
import dev.kgbier.kgbmd.ui.theme.Tonal

@Composable
fun ToolbarIconButton(
    icon: ImageVector,
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
            content = { Icon(icon, contentDescription = tooltip) })
    }
}

@Composable
fun ImageScreen(image: Image) {
    val imageViewerState = rememberImageViewerState()
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            FlexibleBottomAppBar(
                containerColor = Tonal.scrimDark.copy(alpha = 0.6f),
                contentColor = Tonal.primaryDark,
            ) {
                val backEventInput = LocalDirectNavigationEventInput.current

                ToolbarIconButton(
                    icon = Icons.Close,
                    onClick = { backEventInput.backCompleted() },
                    tooltip = "Close",
                )
                Spacer(modifier = Modifier.weight(1f))
                if (imageViewerState.isDirty) {
                    ToolbarIconButton(
                        icon = Icons.FitScreen,
                        onClick = { imageViewerState.reset() },
                        tooltip = "Fit Image",
                    )
                }
            }
        }
    ) {
        Box {
            ImageViewerContainer(
                image = image,
                state = imageViewerState,
                contentPadding = PaddingValues(16.dp)
            )
        }
    }
}
