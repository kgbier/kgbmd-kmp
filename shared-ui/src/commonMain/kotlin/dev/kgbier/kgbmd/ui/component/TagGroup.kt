package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.atom.Tag
import dev.kgbier.kgbmd.ui.component.atom.TagDefaults
import dev.kgbier.kgbmd.ui.component.preview.AppPreview

@Composable
fun TagGroup(
    tags: List<String>,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
        modifier = modifier,
    ) {
        tags.forEach { tag ->
            Tag(
                colors = TagDefaults.outlinedTagColors(),
                border = TagDefaults.outlinedTagBorder(),
                fontWeight = FontWeight.Normal,
            ) { Text(text = tag) }
        }
    }
}

@Composable
@AppPreview.Component
fun TagGroupPreview() {
    TagGroup(
        tags = listOf(
            "Action",
            "Adventure",
            "Sci-Fi",
            "Comedy",
            "Drama",
            "Horror",
            "Mystery",
            "Romance",
            "Thriller",
        ),
        modifier = Modifier.width(240.dp)
    )
}