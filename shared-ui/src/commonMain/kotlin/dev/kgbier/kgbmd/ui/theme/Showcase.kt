package dev.kgbier.kgbmd.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
private fun ColourSchemeShowcase(modifier: Modifier = Modifier) {
    val colourScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Colour Scheme Showcase",
            style = MaterialTheme.typography.headlineMedium,
            color = colourScheme.onSurface
        )

        ThemeGroup(title = "Main Colours") {
            ContentColourCard(
                colour = colourScheme.primary,
                colourName = "Primary",
                contentColour = colourScheme.onPrimary,
                contentColourName = "On Primary",
            )
            ContentColourCard(
                colour = colourScheme.secondary,
                colourName = "Secondary",
                contentColour = colourScheme.onSecondary,
                contentColourName = "On Secondary",
            )
            ContentColourCard(
                colour = colourScheme.tertiary,
                colourName = "Tertiary",
                contentColour = colourScheme.onTertiary,
                contentColourName = "On Tertiary",
            )
        }

        ThemeGroup(title = "Container Colours") {
            ContentColourCard(
                colour = colourScheme.primaryContainer,
                colourName = "Primary Container",
                contentColour = colourScheme.onPrimaryContainer,
                contentColourName = "On Primary Container",
            )
            ContentColourCard(
                colour = colourScheme.secondaryContainer,
                colourName = "Secondary Container",
                contentColour = colourScheme.onSecondaryContainer,
                contentColourName = "On Secondary Container",
            )
            ContentColourCard(
                colour = colourScheme.tertiaryContainer,
                colourName = "Tertiary Container",
                contentColour = colourScheme.onTertiaryContainer,
                contentColourName = "On Tertiary Container",
            )
            ContentColourCard(
                colour = colourScheme.background,
                colourName = "Background",
                contentColour = colourScheme.onBackground,
                contentColourName = "On Background",
            )
            ContentColourCard(
                colour = colourScheme.surface,
                colourName = "Surface",
                contentColour = colourScheme.onSurface,
                contentColourName = "On Surface",
            )
            ContentColourCard(
                colour = colourScheme.surfaceVariant,
                colourName = "Surface Variant",
                contentColour = colourScheme.onSurfaceVariant,
                contentColourName = "On Surface Variant",
            )
            ContentColourCard(
                colour = colourScheme.inverseSurface,
                colourName = "Surface Inverse",
                contentColour = colourScheme.inverseOnSurface,
                contentColourName = "On Surface Inverse",
            )
        }

        ThemeGroup(title = "Error Colours") {
            ContentColourCard(
                colour = colourScheme.error,
                colourName = "Error",
                contentColour = colourScheme.onError,
                contentColourName = "On Error",
            )
            ContentColourCard(
                colour = colourScheme.errorContainer,
                colourName = "Error Container",
                contentColour = colourScheme.onErrorContainer,
                contentColourName = "On Error Container",
            )
        }

        ThemeGroup(title = "Surface") {
            ColourCard(
                colour = colourScheme.surfaceDim,
                colourName = "Surface Dim",
                contentColour = colourScheme.onSurface,
            )
            ColourCard(
                colour = colourScheme.surface,
                colourName = "Surface",
                contentColour = colourScheme.onSurface,
            )
            ColourCard(
                colour = colourScheme.surfaceBright,
                colourName = "Surface Bright",
                contentColour = colourScheme.onSurface,
            )
        }

        ThemeGroup(title = "Surface Container") {
            val size = DpSize(116.dp, 120.dp)
            ColourCard(
                colour = colourScheme.surfaceContainerLowest,
                colourName = "Surface Container Lowest",
                contentColour = colourScheme.onSurface,
                size = size,
            )
            ColourCard(
                colour = colourScheme.surfaceContainerLow,
                colourName = "Surface Container Low",
                contentColour = colourScheme.onSurface,
                size = size,
            )
            ColourCard(
                colour = colourScheme.surfaceContainer,
                colourName = "Surface Container",
                contentColour = colourScheme.onSurface,
                size = size,
            )
            ColourCard(
                colour = colourScheme.surfaceContainerHigh,
                colourName = "Surface Container High",
                contentColour = colourScheme.onSurface,
                size = size,
            )
            ColourCard(
                colour = colourScheme.surfaceContainerHighest,
                colourName = "Surface Container Highest",
                contentColour = colourScheme.onSurface,
                size = size,
            )
        }
    }
}

@Composable
private fun ThemeGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun ColourCard(
    colour: Color,
    colourName: String,
    contentColour: Color,
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(192.dp, 72.dp),
) {
    Box(
        modifier = modifier
            .size(size)
            .background(colour)
            .padding(12.dp)
    ) {
        Text(
            text = colourName,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColour,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Text(
            text = colour.toHexString(),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = contentColour.copy(alpha = 0.7f),
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun ContentColourCard(
    colour: Color,
    colourName: String,
    contentColour: Color,
    contentColourName: String,
) = Column {
    ColourCard(
        colour = colour,
        colourName = colourName,
        contentColour = contentColour,
    )
    ColourCard(
        colour = contentColour,
        colourName = contentColourName,
        contentColour = colour,
        size = DpSize(width = 192.dp, height = 64.dp)
    )
}

private fun Color.toHexString(): String {
    return "#${value.shr(32).toInt().toHexString()}"
}

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "1. Light", widthDp = 640, heightDp = 1300)
@Preview(
    name = "2. Dark",
    widthDp = 640,
    heightDp = 1300,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
private annotation class PreviewColourScheme

@PreviewColourScheme
@Composable
private fun ColourSchemeAppShowcasePreview() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ColourSchemeShowcase()
        }
    }
}

@PreviewColourScheme
@Composable
private fun ColourSchemeMaterialShowcasePreview() {
    MaterialTheme(
        colorScheme = when (isSystemInDarkTheme()) {
            true -> darkColorScheme()
            false -> lightColorScheme()
        }
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ColourSchemeShowcase()
        }
    }
}
