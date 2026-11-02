package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import dev.kgbier.kgbmd.domain.model.Image
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.ui.component.CastCreditRow
import dev.kgbier.kgbmd.ui.component.Histogram
import dev.kgbier.kgbmd.ui.component.SubtitlePosterView
import dev.kgbier.kgbmd.ui.component.atom.SubtitleText
import dev.kgbier.kgbmd.ui.component.atom.Tag
import dev.kgbier.kgbmd.ui.component.atom.TagDefaults
import dev.kgbier.kgbmd.ui.component.icon.Icons
import dev.kgbier.kgbmd.ui.component.itemSpacingMain
import dev.kgbier.kgbmd.ui.component.molecule.ExpandableDescription
import dev.kgbier.kgbmd.ui.component.molecule.TitledContent
import dev.kgbier.kgbmd.ui.component.preview.AppPreview
import dev.kgbier.kgbmd.ui.component.preview.sampleTitleDetails
import dev.kgbier.kgbmd.ui.nav.Router
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.util.itemSpacing

@Composable
fun TitleDetailsScreen(
    title: TitleDetails,
    router: Router<AppRoute>,
    contentPadding: PaddingValues = PaddingValues(),
    onShowModal: (DetailsModalRoute) -> Unit = {},
) = TitleDetailsScreen(
    title = title,
    contentPadding = contentPadding,
    onHeaderPosterViewClicked = { router.push(AppRoute.ImageViewer(it)) },
    onFeaturedCreditClicked = { router.push(AppRoute.Details(it)) },
    onEpisodePosterClicked = { router.push(AppRoute.Details(it)) },
    onCastCreditClicked = { router.push(AppRoute.Details(it)) },
    onShowModal = onShowModal,
)

@Composable
fun TitleDetailsScreen(
    title: TitleDetails,
    contentPadding: PaddingValues = PaddingValues(),
    onShowModal: (DetailsModalRoute) -> Unit = {},
    onHeaderPosterViewClicked: (Image) -> Unit = {},
    onFeaturedCreditClicked: (MediaEntityId) -> Unit = {},
    onEpisodePosterClicked: (MediaEntityId) -> Unit = {},
    onCastCreditClicked: (MediaEntityId) -> Unit = {},
) = LazyColumn(
    contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
) {
    item {
        TitleDetailsHeader(
            title = title,
            onPosterViewClicked = { onHeaderPosterViewClicked(it) },
            modifier = Modifier
                .padding(16.dp)
                .padding(top = contentPadding.calculateTopPadding()),
        )
    }

    itemSpacingMain()

    item {
        TitlePrincipalCredits(
            title = title,
            onFeaturedCreditClicked = onFeaturedCreditClicked,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    itemSpacingMain()

    item {
        title.description?.let { description ->
            ExpandableDescription(
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
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
                TextButton(onClick = {
                    onShowModal(DetailsModalRoute.Episodes(title.id))
                }) {
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
                        onClick = { onEpisodePosterClicked(episode.moviePoster.id) },
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
                TextButton(onClick = {
                    onShowModal(DetailsModalRoute.CastAndCrew(title.id))
                }) {
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
                onClick = { onCastCreditClicked(castCredit.nameProfile.id) },
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }


    val watchOption = title.watchOption
    if (watchOption != null) {
        itemSpacingMain()

        item {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                )
            ) {
                Text(
                    text = "Watch",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        itemSpacing(8.dp)

        item {
            when(watchOption) {
                TitleDetails.WatchOption.Cinema -> Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.ConfirmationNumber, contentDescription = "")
                    Text("In Theatres")
                }
                is TitleDetails.WatchOption.Watchable -> WatchOptionItemRow(
                    watchOption = watchOption,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    itemSpacingMain()

    item {
        Text(
            text = "Awards",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }


    val ratingsBreakdown = title.ratingsBreakdown
    if (ratingsBreakdown != null) {
        itemSpacingMain()

        item {
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        itemSpacing(8.dp)

        item {
            val values = remember(ratingsBreakdown) { ratingsBreakdown.values.map { it.count } }
            Histogram(
                values = values,
                labelContent = { index ->
                    Text(ratingsBreakdown.values[index].rating)
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(96.dp)
            )
        }
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

@Composable
private fun WatchOptionItemRow(
    watchOption: TitleDetails.WatchOption.Watchable,
    modifier: Modifier = Modifier,
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
) {
    AsyncImage(
        contentDescription = null,
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(watchOption.image?.smallUrl)
            .build(),
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .size(height = 48.dp, width = 86.dp)
    )
    Spacer(modifier = Modifier.width(16.dp))
    Column {
        SubtitleText(watchOption.title)
        watchOption.description?.let {
            Text(text = it)
        }
    }
}

@AppPreview.Screen
@Composable
fun TitleDetailsScreenPreview() {
    TitleDetailsScreen(
        title = sampleTitleDetails,
    )
}
