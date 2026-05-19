package dev.kgbier.kgbmd.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.atom.Tag
import dev.kgbier.kgbmd.ui.theme.AppTheme

data class CreditGroup(
    val id: String,
    val category: String,
    val total: Int,
)

@Composable
fun GroupedCreditsList(
    contentPadding: PaddingValues = PaddingValues(),
) {

    val expandedSectionIdsMap: SnapshotStateMap<String, Boolean> = remember { mutableStateMapOf() }

    val categories = listOf(
        CreditGroup("cat:1", "Director", 1),
        CreditGroup("cat:2", "Writer", 1),
        CreditGroup("cat:3", "Cast", 64),
        CreditGroup("cat:4", "Producers", 7),
        CreditGroup("cat:5", "Composer", 10),
        CreditGroup("cat:6", "Cinematographer", 77),
        CreditGroup("cat:7", "Editor", 12),
        CreditGroup("cat:8", "Casting", 54),
        CreditGroup("cat:9", "Production Designer", 103),
        CreditGroup("cat:10", "Costume Designer", 1004),
        CreditGroup("cat:11", "Makeup Department", 3),
        CreditGroup("cat:12", "Second Unit Directors or Assistant Directors", 3),
    )
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPadding,
    ) {
        categories.forEach { item ->
            val isExpanded = expandedSectionIdsMap[item.id] ?: false

            stickyHeader(
                key = item.id,
                contentType = "Header",
            ) {
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isExpanded) 90f else 0f,
                    label = "ChevronRotation"
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier
                        .clickable { expandedSectionIdsMap[item.id] = !isExpanded }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = item.category,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .alignByBaseline()
                                .weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Tag(
                            modifier = Modifier.alignByBaseline()
                        ) {
                            Text(item.total.toString())
                        }
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                }
            }
            if (isExpanded) {
                items(
                    items = List(item.total) { "${item.id}:$it" },
                    contentType = { "Item" },
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupedCreditsListPreview() = AppTheme(darkTheme = true) {
    GroupedCreditsList()
}
