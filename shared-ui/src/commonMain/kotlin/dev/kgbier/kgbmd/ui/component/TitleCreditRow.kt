package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.molecule.RatingStarView

@Composable
fun TitleCreditRow(
    title: String,
    roles: List<String>,
    rating: String? = null,
    year: String? = null,
    status: String? = null,
    onClick: (() -> Unit)? = null,
    posterImageUrl: String? = null,
    contentPadding: PaddingValues = PaddingValues(6.dp),
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                enabled = onClick != null,
                onClick = onClick ?: {},
            ).padding(contentPadding)
    ) {

        PosterView(
            thumbnailUrl = null,
            imageUrl = posterImageUrl,
            onClick = { },
            enabled = false,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .size(40.dp, 58.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (year != null || status != null) {
                Row {
                    year?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                    if (year != null && status != null) {
                        Text(
                            text = " · ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    status?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val roleText = remember(roles) {
                roles.takeIf { it.isNotEmpty() }?.joinToString(", ")
            }
            roleText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        rating?.let {
            RatingStarView(
                ratingText = it,
                textStyle = MaterialTheme.typography.bodyMedium,
                textColour = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
fun TitleCreditRowPreview() = MaterialTheme {
    TitleCreditRow(
        title = "Title Name",
        rating = "7.6",
        roles = listOf("Role Name", "Other Role"),
        year = "2006",
        status = "Post-production",
    )
}
