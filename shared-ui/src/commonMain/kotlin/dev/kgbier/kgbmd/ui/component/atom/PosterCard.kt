package dev.kgbier.kgbmd.ui.component.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PosterCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    content: @Composable ColumnScope.() -> Unit,
) = Card(
    onClick = onClick,
    enabled = enabled,
    shape = shape,
    elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
        pressedElevation = 8.dp,
    ),
    content = content,
    modifier = modifier
        .aspectRatio(100f / 148f) // Poster aspect ratio
)

@Composable
@Preview
fun PosterCardPreview() = PosterCard(
    onClick = {},
) {
    Box(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .background(Color.White)
            .align(Alignment.CenterHorizontally)
    )
}
