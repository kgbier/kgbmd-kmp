package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kgbmd.shared_ui.generated.resources.Res
import kgbmd.shared_ui.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource

@Composable
fun RatingStarView(
    ratingText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    textColour: Color = Color.Unspecified,
    starSizeMultiplier: Float = 0.8f,
    spacing: Dp = 2.dp,
) = Row(
    modifier = modifier
        .height(intrinsicSize = IntrinsicSize.Max),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = ratingText,
        style = textStyle,
        color = textColour,
    )

    Spacer(modifier = Modifier.width(spacing))

    val starImage: Painter = painterResource(Res.drawable.ic_star)

    Image(
        painter = starImage,
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight(starSizeMultiplier)
    )
}

@Composable
@Preview
fun RatingStarViewPreview() = MaterialTheme {
    RatingStarView(
        ratingText = "7.9",
    )
}
