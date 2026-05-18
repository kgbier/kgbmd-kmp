package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.ui.component.SubtitlePosterView
import dev.kgbier.kgbmd.ui.component.itemSpacingMain
import dev.kgbier.kgbmd.ui.component.molecule.ExpandableDescription
import dev.kgbier.kgbmd.ui.component.molecule.TitledContent
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.itemSpacing

@Composable
fun NameDetailsScreen(
    name: NameDetails,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
) = LazyColumn(
    contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
) {
    item {
        NameDetailsHeader(
            name = name,
            modifier = Modifier
                .padding(16.dp)
                .padding(top = contentPadding.calculateTopPadding())
        )
    }

    name.description?.let { description ->
        item {
            ExpandableDescription(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
            ) {
                TitledContent(
                    title = "Biography",
                    body = description,
                )
            }
        }
    }

    val hasTopCredits = name.topCredits.isNotEmpty()

    if (hasTopCredits) {
        itemSpacing(8.dp)
        item {
            Text(
                text = "Known For",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        itemSpacingMain()

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp, Alignment.CenterHorizontally
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(name.topCredits) { credit ->
                    SubtitlePosterView(
                        poster = credit.poster,
                        subtitle = credit.role,
                        onClick = { router.push(AppRoute.Details(credit.poster.id)) },
                    )
                }
            }
        }
    }
}
