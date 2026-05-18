package dev.kgbier.kgbmd.ui.component.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class TagColors(
    val containerColor: Color,
    val contentColor: Color,
)

object TagDefaults {

    @Composable
    fun filledTagColors() = MaterialTheme.colorScheme.defaultTagColors

    @Composable
    fun filledTonalTagColors() = MaterialTheme.colorScheme.filledTonalTagColors

    @Composable
    fun outlinedTagColors() = MaterialTheme.colorScheme.defaultOutlinedTagColors

    @Composable
    fun outlinedTagBorder(): BorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.inverseSurface,
    )

    internal val ColorScheme.defaultTagColors: TagColors
        get() = TagColors(
            containerColor = inverseSurface,
            contentColor = inverseOnSurface,
        )

    internal val ColorScheme.filledTonalTagColors: TagColors
        get() = TagColors(
            containerColor = primaryContainer,
            contentColor = onPrimaryContainer,
        )

    internal val ColorScheme.defaultOutlinedTagColors: TagColors
        get() = TagColors(
            containerColor = Color.Transparent,
            contentColor = onSurface,
        )
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    colors: TagColors = TagDefaults.filledTagColors(),
    border: BorderStroke? = null,
//    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = FontWeight.ExtraBold,
    startContent: @Composable () -> Unit = {},
    endContent: @Composable () -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(4.dp),
    content: @Composable () -> Unit,
) = Row(
    horizontalArrangement = horizontalArrangement,
    modifier = modifier
        .then(
            if (border != null) {
                Modifier.border(border, shape)
            } else {
                Modifier.background(colors.containerColor, shape)
            }
        )
        .clip(shape)
        .padding(horizontal = 6.dp)
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.labelSmall
            .copy(fontWeight = fontWeight),
        LocalContentColor provides colors.contentColor,
    ) {
        startContent()
        content()
        endContent()
    }
}

@Preview
@Composable
private fun TagDefaultPreview() = MaterialTheme {
    Tag {
        Text("Label")
    }
}

@Preview
@Composable
private fun TagFilledTonalPreview() = MaterialTheme {
    Tag(
        colors = TagDefaults.filledTonalTagColors(),
    ) {
        Text("Label")
    }
}

@Preview
@Composable
private fun TagContentSlotPreview() = MaterialTheme {
    Tag(
        startContent = { Box(modifier = Modifier.size(8.dp).background(Color.Red)) },
        endContent = { Box(modifier = Modifier.background(Color.Blue).size(4.dp)) },
    ) {
        Text("Label")
    }
}

@Preview
@Composable
private fun TagOutlinePreview() = MaterialTheme {
    Tag(
        colors = TagDefaults.outlinedTagColors(),
        border = TagDefaults.outlinedTagBorder()
    ) {
        Text("Label")
    }
}
