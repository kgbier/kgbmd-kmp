package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import dev.kgbier.kgbmd.ui.component.atom.Portrait

@Composable
fun CastCreditRow(
    name: String,
    roles: List<String>,
    onClick: (() -> Unit)? = null,
    creditImageUrl: String? = null,
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

        Portrait(
            creditImageUrl = creditImageUrl,
            modifier = Modifier.size(48.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = name,
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
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun CastCreditRowPreview() = MaterialTheme {
    CastCreditRow(
        name = "Credit Name",
        roles = listOf("Role Name", "Other Role"),
    )
}
