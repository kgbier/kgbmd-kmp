package dev.kgbier.kgbmd.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.ui.component.molecule.PortraitCard
import dev.kgbier.kgbmd.ui.component.preview.AppPreview

@Composable
fun FeaturedCreditCard(
    name: String,
    onClick: (() -> Unit)? = null,
    creditImageUrl: String? = null,
    contentPadding: PaddingValues = PaddingValues(6.dp),
    modifier: Modifier = Modifier,
) = PortraitCard(
    onClick = onClick,
    portraitImageUrl = creditImageUrl,
    contentPadding = contentPadding,
    modifier = modifier,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
@AppPreview.Component
fun FeaturedCreditCardPreview() {
    FeaturedCreditCard(
        name = "Name",
        onClick = {},
    )
}