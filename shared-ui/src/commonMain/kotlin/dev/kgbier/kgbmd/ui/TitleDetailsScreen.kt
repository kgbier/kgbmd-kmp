package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.ui.component.CastCreditRow
import dev.kgbier.kgbmd.ui.component.SubtitlePosterView
import dev.kgbier.kgbmd.ui.component.atom.Tag
import dev.kgbier.kgbmd.ui.component.atom.TagDefaults
import dev.kgbier.kgbmd.ui.component.itemSpacingMain
import dev.kgbier.kgbmd.ui.component.molecule.ExpandableDescription
import dev.kgbier.kgbmd.ui.component.molecule.TitledContent
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.itemSpacing

@Composable
fun TitleDetailsScreen(
    title: TitleDetails,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
) = LazyColumn(
    contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
) {
    item {
        TitleDetailsHeader(
            title = title,
            modifier = Modifier
                .padding(16.dp)
                .padding(top = contentPadding.calculateTopPadding())
        )
    }

    itemSpacingMain()

    item {
        TitlePrincipalCredits(
            title = title,
            router = router,
            modifier = Modifier.fillMaxWidth()
        )
    }

    item {
        title.description?.let { description ->
            ExpandableDescription(
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
            ) {
                TitledContent(
                    title = "Summary",
                    body = description,
                )
            }
        }
    }

    val topEpisodes = title.topEpisodes
    if (topEpisodes != null) {
        itemSpacing(8.dp)
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, end = 6.dp)
            ) {
                Text(
                    text = "Top Episodes",
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(Modifier.weight(1f))
                TextButton(onClick = {}) {
                    Text("All")
                    Spacer(modifier = Modifier.width(8.dp))
                    Tag(
                        colors = TagDefaults.filledTonalTagColors(),
                    ) { Text("${topEpisodes.episodeCount}") }
                }
            }
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
                items(topEpisodes.episodes) { episode ->
                    SubtitlePosterView(
                        poster = episode.moviePoster,
                        subtitle = "S${episode.season}:E${episode.number}",
                        onClick = { router.push(AppRoute.Details(episode.moviePoster.id)) },
                    )
                }
            }
        }
    }

    val topCast = title.topCast
    if (topCast != null) {
        itemSpacingMain()

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, end = 6.dp)
            ) {
                Text(
                    text = "Top Cast",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = {}) {
                    Text("Full credits")
                }
            }
        }

        itemSpacing(8.dp)

        items(topCast.topCast) { castCredit ->
            CastCreditRow(
                name = castCredit.nameProfile.name,
                roles = castCredit.roles,
                creditImageUrl = castCredit.nameProfile.photo?.thumbnailUrl,
                onClick = { router.push(AppRoute.Details(castCredit.nameProfile.id)) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        itemSpacingMain()

        item {
            Text(
                text = "Awards",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        itemSpacingMain()

        item {
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        itemSpacingMain()

        item {
            Text(
                text = "Box Office",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        itemSpacingMain()
    }
}
