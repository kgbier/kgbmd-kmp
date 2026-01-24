@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.ui.component.CreditPortrait
import dev.kgbier.kgbmd.ui.component.LazyHeadingRow
import dev.kgbier.kgbmd.ui.component.SubtitleText
import dev.kgbier.kgbmd.ui.component.Tag
import dev.kgbier.kgbmd.ui.component.TitledContent
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import dev.kgbier.kgbmd.ui.nav.Navigator
import dev.kgbier.kgbmd.ui.route.AppRoute
import dev.kgbier.kgbmd.ui.theme.Tonal
import dev.kgbier.kgbmd.ui.util.ExpandedWidth
import kotlinx.coroutines.CoroutineScope

@Composable
fun DetailsScreen(
    id: MediaEntityId,
    navigator: Navigator<AppRoute>,
    viewModelFactory: DetailsScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: DetailsScreenViewModel = remember(id) {
        viewModelFactory.createDetailsScreenViewModelFactory(scope, id)
    }
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = ExpandedWidth.dp)
            ) {
                if (state == null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingIndicator()
                    }
                }

                val screenScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(screenScrollState)
                        .padding(
                            bottom = WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateBottomPadding(),
                        )
                ) {
                    when (val state = state) {
                        is TitleDetails -> TitleDetails(state, navigator)

                        is NameDetails -> NameDetails(state)

                        null -> Unit // Loading
                    }
                }
            }
        }
    }
}

@Composable
private fun NameDetails(name: NameDetails) {
    Spacer(
        modifier = Modifier
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    Text(
        text = name.name,
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        "Description",
        style = MaterialTheme.typography.labelSmall,
    )
    Text(
        text = name.description.toString(),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun TitleDetails(
    title: TitleDetails,
    navigator: Navigator<AppRoute>
) = Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    val shadowSize = 16.dp
    TitleDetailsHeading(
        title = title,
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .dropShadow(GenericShape { size, _ ->
                addRect(
                    Rect(
                        size = size.copy(height = shadowSize.value * 2),
                        offset = Offset(x = 0f, y = size.height - (shadowSize.value / 2))
                    )
                )
            }, Shadow(shadowSize))
            .padding(16.dp)
            .padding(
                top = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateTopPadding(),
            )
    )

    TitlePrincipalCredits(
        title = title,
        navigator = navigator,
    )

    title.description?.let { description ->
        TitledContent(
            title = "Summary",
            body = description,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    Text(
        text = "Cast",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun TitleDetailsHeading(
    title: TitleDetails,
    modifier: Modifier = Modifier,
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    TitleDetailsHeadingBackground(
        title = title,
        modifier = Modifier
            .matchParentSize()
    )
    TitleDetailsHeadingForeground(
        title = title,
        modifier = modifier
    )
}

@Composable
private fun TitleDetailsHeadingBackground(
    title: TitleDetails,
    modifier: Modifier = Modifier,
) = AsyncImage(
    model = title.poster?.thumbnailUrl,
    contentDescription = null,
    contentScale = ContentScale.Crop,
    colorFilter = ColorFilter.tint(
        color = Tonal.scrimDark.copy(alpha = 0.7f),
        blendMode = BlendMode.Darken,
    ),
    modifier = modifier.blur(
        radiusX = 20.dp,
        radiusY = 20.dp,
    )
)

@Composable
private fun TitleDetailsHeadingForeground(
    title: TitleDetails,
    modifier: Modifier = Modifier,
) = Column(
    verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
) {
    val heading = buildAnnotatedString {
        append(title.name)
        title.yearReleased?.let {
            pushStyle(
                MaterialTheme.typography.titleMedium.toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            append("${Typography.nbsp}(${title.yearReleased})")
        }
    }
    Text(
        text = heading,
        style = MaterialTheme.typography.headlineSmall,
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        title.contentRating?.let { contentRating ->
            Tag { Text(contentRating) }
        }
        title.duration?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.typography.labelSmall.fontSize.value.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = it, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        title.rating?.let { rating ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = rating.value, style = MaterialTheme.typography.titleLarge
                    )
                    RatingStarView(
                        ratingText = " / ${rating.best}",
                        textStyle = MaterialTheme.typography.titleMedium,
                        textColour = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                rating.count?.let { count ->
                    Text(
                        text = count,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        title.poster?.thumbnailUrl?.let { thumbnailUrl ->
            PosterView(
                thumbnailUrl = null,
                imageUrl = thumbnailUrl,
                onClick = { },
                enabled = false,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.size(40.dp, 58.dp).align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun TitlePrincipalCredits(
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
                heading, modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
            )
        },
        modifier = modifier,
    ) {
        items(principalCredits) { item ->
            CreditPortrait(
                name = item.name,
                onClick = { navigator.push(AppRoute.Details(item.id)) },
                creditImageUrl = item.photo?.thumbnailUrl
            )
        }
    }
}

