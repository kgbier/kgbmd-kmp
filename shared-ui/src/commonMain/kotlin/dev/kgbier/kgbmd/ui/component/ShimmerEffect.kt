package dev.kgbier.kgbmd.ui.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun rememberShimmerState(): Float {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 1200,
                easing = CubicBezierEasing(0.2f, 0.9f, 0.2f, 0.9f),
            ),
        ),
        label = "shimmer",
    )
    return progress
}

@Composable
fun ShimmerEffect(
    state: Float,
    modifier: Modifier = Modifier,
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.0f),
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.0f),
    )
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val scale = (state * size.width * 6)
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            tileMode = TileMode.Mirror,
            start = Offset(
                x = scale - (size.width * 1.25f),
                y = 0f,
            ),
            end = Offset(
                x = scale,
                y = size.height,
            )
        )
        drawRect(brush = brush)
    }
}

@Preview(widthDp = 30, heightDp = 40)
@Composable
fun ShimmerEffectPreview() = MaterialTheme {
    Surface {
        ShimmerEffect(0.2f)
    }
}
