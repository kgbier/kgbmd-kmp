package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    startContent: @Composable () -> Unit = {},
    endContent: @Composable () -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(4.dp),
    content: @Composable () -> Unit,
) = Row(
    horizontalArrangement = horizontalArrangement,
    modifier = modifier
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.inverseSurface)
        .padding(horizontal = 6.dp)
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.labelSmall
            .copy(fontWeight = FontWeight.ExtraBold),
        LocalContentColor provides MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        startContent()
        content()
        endContent()
    }
}


@Preview
@Composable
private fun TagPreview() = MaterialTheme {
    Tag {
        Text("Label")
    }
}
