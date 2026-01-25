package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.ui.component.CreditPortrait
import dev.kgbier.kgbmd.ui.component.LazyHeadingRow
import dev.kgbier.kgbmd.ui.component.SubtitleText
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.route.AppRoute

@Composable
fun TitlePrincipalCredits(
    title: TitleDetails,
    navigator: Navigator<AppRoute>,
    modifier: Modifier = Modifier,
) {
    val headings = remember(title.principalCreditsByGroup) {
        var lastLength = 0
        title.principalCreditsByGroup.map { (key, value) ->
            LazyHeadingRow.Heading(lastLength, key).also { lastLength += value.size }
        }
    }
    val principalCredits = remember(title.principalCreditsByGroup) {
        title.principalCreditsByGroup.values.flatten()
    }

    LazyHeadingRow(
        headings = headings,
        contentPadding = PaddingValues(
            horizontal = 10.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        headingContent = { heading ->
            SubtitleText(
                heading,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            )
        },
        modifier = modifier,
    ) {
        items(principalCredits) { item ->
            CreditPortrait(
                name = item.name,
                onClick = { navigator.push(AppRoute.Details(item.id)) },
                creditImageUrl = item.photo?.thumbnailUrl,
                modifier = Modifier.sizeIn(maxWidth = 90.dp)
            )
        }
    }
}
