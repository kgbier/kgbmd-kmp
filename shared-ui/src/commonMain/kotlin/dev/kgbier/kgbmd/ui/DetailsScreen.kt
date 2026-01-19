package dev.kgbier.kgbmd.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.NameDetails
import dev.kgbier.kgbmd.domain.model.TitleDetails
import dev.kgbier.kgbmd.presentation.DetailsScreenViewModel
import dev.kgbier.kgbmd.ui.component.LazyHeadingRow
import dev.kgbier.kgbmd.ui.di.LocalViewModelModule
import kotlinx.coroutines.CoroutineScope


@Composable
fun SettingsScreen() = Scaffold {
    LazyHeadingRow(
        headings = listOf(
            LazyHeadingRow.Heading(0, "Director"),
            LazyHeadingRow.Heading(1, "Writers"),
            LazyHeadingRow.Heading(5, "Stars"),
        ),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        headingContent = { Text(it) },
    ) {
        items(100) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun DetailsScreen(
    id: MediaEntityId,
    viewModelFactory: DetailsScreenViewModel.Factory = LocalViewModelModule.current,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: DetailsScreenViewModel = remember {
        viewModelFactory.createDetailsScreenViewModelFactory(scope, id)
    }
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = ExpandedWidth.dp)
            ) {
                Column {
                    val title = state
                    if (title is TitleDetails) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
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
                            title.duration?.let {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(MaterialTheme.typography.labelSmall.fontSize.value.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(text = it, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            title.rating?.let { rating ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = rating.value,
                                            style = MaterialTheme.typography.titleLarge
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
                        }

                        val headings = remember(title.principalCreditsByGroup) {
                            var lastLength = 0
                            title.principalCreditsByGroup.map { (key, value) ->
                                LazyHeadingRow.Heading(lastLength, key)
                                    .also { lastLength += value.size }
                            }
                        }
                        val principalCredits = remember(title.principalCreditsByGroup) {
                            title.principalCreditsByGroup.values.flatten()
                        }

                        LazyHeadingRow(
                            headings = headings,
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            headingContent = { heading ->
                                Text(
                                    heading,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        ) {
                            items(principalCredits) { item ->
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        Text(
                            "Summary",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Text(
                            text = title.description.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cast",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    val name = state
                    if (name is NameDetails) {
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
                }
            }
        }
    }
}