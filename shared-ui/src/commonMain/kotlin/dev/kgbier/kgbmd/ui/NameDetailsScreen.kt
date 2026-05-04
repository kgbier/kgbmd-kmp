package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.ui.component.PosterView
import dev.kgbier.kgbmd.ui.component.atom.SubtitleText
import dev.kgbier.kgbmd.ui.component.molecule.TitledContent
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute

@Composable
fun NameDetailsScreen(
    name: NameDetails,
    router: Router<AppRoute>,
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    NameDetailsHeader(
        name = name,
        modifier = Modifier
            .padding(16.dp)
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    name.description?.let { description ->
        TitledContent(
            title = "Biography",
            body = description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    val hasTopCredits = name.topCredits.isNotEmpty()

    if (hasTopCredits) {
        Text(
            text = "Known For",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp, Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(name.topCredits.size) { index ->
                val credit = name.topCredits[index]
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .widthIn(max = 100.dp)
                ) {
                    PosterView(
                        title = credit.name,
                        thumbnailUrl = credit.poster?.thumbnailUrl,
                        imageUrl = credit.poster?.largeUrl,
                        onClick = { router.push(AppRoute.Details(credit.id)) },
                        rating = credit.rating,
                    )
                    credit.role?.let { roles ->
                        SubtitleText(
                            text = roles,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    Spacer(
        modifier = Modifier
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )
}