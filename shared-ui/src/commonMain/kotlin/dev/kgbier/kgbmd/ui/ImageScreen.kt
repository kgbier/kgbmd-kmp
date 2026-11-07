@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FlexibleBottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.Image
import dev.kgbier.kgbmd.ui.component.ImageViewerContainer
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.molecule.ToolbarIconButton
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.component.preview.sampleImage
import dev.kgbier.kgbmd.ui.component.rememberImageViewerState
import dev.kgbier.kgbmd.ui.nav.LocalDirectNavigationEventInput
import dev.kgbier.kgbmd.ui.theme.Tonal

@Composable
fun ImageScreen(
    image: Image,
) {
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

@Composable
@AppPreview.Screen
fun ImageScreenPreview() {
    ImageScreen(
        image = sampleImage,
    )
}
