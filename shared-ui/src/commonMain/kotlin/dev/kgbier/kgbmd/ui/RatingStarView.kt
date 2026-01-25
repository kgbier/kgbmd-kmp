package dev.kgbier.kgbmd.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kgbmd.shared_ui.generated.resources.Res
import kgbmd.shared_ui.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource

private const val StarPoints = 5
private const val FullRotation = 360f
private const val PointRotation = FullRotation / StarPoints
private const val SpinRotation = (FullRotation + (PointRotation * 0.9f))

@Composable
fun RatingStarView(
    ratingText: String,
    modifier: Modifier = Modifier,
    isSpinning: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    textColour: Color = Color.Unspecified,
    starSizeMultiplier: Float = 0.8f,
    spacing: Dp = 2.dp,
) = Row(
    modifier = modifier
        .height(intrinsicSize = IntrinsicSize.Max),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(spacing),
) {
    Text(
        text = ratingText,
        style = textStyle,
        color = textColour,
    )

    val starImage: Painter = painterResource(Res.drawable.ic_star)

    val angle = if (isSpinning) {
        val spin = rememberInfiniteTransition("spin infinitely")

        spin.animateFloat(
            initialValue = 0f,
            targetValue = if (isSpinning) SpinRotation else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "spin"
        ).value
    } else 0f

    Image(
        painter = starImage,
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight(starSizeMultiplier)
            .graphicsLayer { rotationZ = angle }
    )
}

@Composable
@Preview
fun RatingStarViewPreview() = MaterialTheme {
    RatingStarView(
        ratingText = "7.9",
    )
}
