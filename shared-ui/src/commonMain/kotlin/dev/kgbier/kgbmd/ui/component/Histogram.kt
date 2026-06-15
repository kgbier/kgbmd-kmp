package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Histogram(
    values: List<Long>,
    modifier: Modifier = Modifier.Companion,
    color: Color = MaterialTheme.colorScheme.primary,
    labelContent: @Composable (index: Int) -> Unit = {
        Text(it.toString())
    },
) {
    if (values.isEmpty()) return
    val maxValue = remember(values) { values.maxOrNull()!!.coerceAtLeast(1L).toFloat() }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .drawBehind {
                    val barSpacing = 4.dp.toPx()
                    val barWidth = (size.width - (barSpacing * (values.size - 1))) / values.size
                    val cornerRadius = CornerRadius(4.dp.toPx())

                    values.forEachIndexed { index, value ->
                        val barHeight = size.height * (value / maxValue).coerceAtLeast(0.01f)
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(
                                index * (barWidth + barSpacing),
                                size.height - barHeight
                            ),
                            size = Size(barWidth, barHeight),
                            cornerRadius = cornerRadius
                        )
                    }
                }
        )
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            values.indices.forEach { index ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                        labelContent(index)
                    }
                }
            }
        }
    }
}

@Preview(heightDp = 192)
@Composable
fun HistogramPreview() = MaterialTheme {
    Histogram(
        listOf(
            123,
            113,
            63,
            23,
            83,
            143,
        )
    )
}
