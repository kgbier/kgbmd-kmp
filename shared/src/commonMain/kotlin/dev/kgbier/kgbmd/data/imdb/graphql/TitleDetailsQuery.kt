package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.TitleDetails
import kotlinx.serialization.Serializable

class TitleDetailsQuery : GraphqlQuery<TitleDetailsQuery.Params, TitleDetailsQuery.Result> {

    @Serializable
    data class Params(val id: String)

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(
            val id: String,
            val titleText: TitleText,
            val titleType: TitleType,
            val primaryImage: PrimaryImage?,
            val releaseYear: ReleaseYear?,
            val certificate: Certificate?,
            val ratingsSummary: RatingsSummary,
            val runtime: Runtime?,
            val titleGenres: TitleGenres,
            val seriesSeasonEpisode: SeriesSeasonEpisodeFragment?,
            val series: Series?,
            val episodes: Episodes?,
            val principalCredits: List<PrincipalCreditsForGrouping>,
            val topCast: List<TopCastCredits>,
            val outline: Plot,
        ) {
            @Serializable
            data class TitleText(val text: String)

            @Serializable
            data class TitleType(val id: String)

            @Serializable
            data class PrimaryImage(val url: String?)

            @Serializable
            data class ReleaseYear(val year: Int?, val endYear: Int?)

            @Serializable
            data class Certificate(val rating: String?)

            @Serializable
            data class RatingsSummary(val aggregateRating: Double?, val voteCount: Int?)

            @Serializable
            data class Runtime(val displayableProperty: DisplayableProperty) {
                @Serializable
                data class DisplayableProperty(val value: Value) {
                    @Serializable
                    data class Value(val plainText: String)
                }
            }

            @Serializable
            data class TitleGenres(val genres: List<Genre>) {
                @Serializable
                data class Genre(val genre: GenreText) {
                    @Serializable
                    data class GenreText(val text: String)
                }
            }

            @Serializable
            data class Series(
                val seriesTitle: SeriesTitle,
                val nextEpisode: AdjacentEpisodeTitle?,
                val previousEpisode: AdjacentEpisodeTitle?,
            ) {
                @Serializable
                data class SeriesTitle(
                    val id: String,
                    val titleText: TitleText,
                ) {
                    @Serializable
                    data class TitleText(val text: String)
                }

                @Serializable
                data class AdjacentEpisodeTitle(val id: String)
            }

            @Serializable
            data class Episodes(
                val isOngoing: Boolean,
                val episodes: EpisodeConnection,
            ) {
                @Serializable
                data class EpisodeConnection(
                    val total: Int,
                    val topEpisodes: List<Edge>,
                ) {
                    @Serializable
                    data class Edge(
                        val poster: TitlePosterFragment,
                        val node: Node,
                    ) {
                        @Serializable
                        data class Node(
                            val seriesSeasonEpisode: SeriesSeasonEpisodeFragment,
                        )
                    }
                }
            }

            @Serializable
            data class PrincipalCreditsForGrouping(
                val grouping: Grouping,
                val credits: List<Credit>,
            ) {
                @Serializable
                data class Grouping(val text: String)

                @Serializable
                data class Credit(val name: NameProfileCreditFragment)
            }

            @Serializable
            data class TopCastCredits(
                val totalCredits: Int,
                val grouping: Grouping,
                val credits: List<RoleCreditFragment>,
            ) {
                @Serializable
                data class Grouping(
                    val groupingId: String,
                )
            }

            @Serializable
            data class Plot(val edges: List<Edge>) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(val plotText: PlotText) {
                        @Serializable
                        data class PlotText(val plainText: String)
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query TitleDetails($id: ID!) {
  title(id: $id) {
    id
    titleText {
      text
    }
    titleType {
      id
    }
    primaryImage {
      url
    }
    releaseYear {
      year
      endYear
    }
    certificate {
      rating
    }
    ratingsSummary {
      aggregateRating
      voteCount
    }
    runtime {
      displayableProperty {
        value {
          plainText
        }
      }
    }
    titleGenres {
      genres {
        genre {
          text
        }
      }
    }
    seriesSeasonEpisode: series {
      ...$${SeriesSeasonEpisodeFragment.name}
    }
    series {
      seriesTitle: series {
        id
        titleText {
          text
        }
      }
      nextEpisode {
        id
      }
      previousEpisode {
        id
      }
    }
    episodes {
      isOngoing
      episodes(first: 10, sort: { order: DESC, by: RATING }) {
        total
        topEpisodes: edges {
          poster: node {
            ...$${TitlePosterFragment.name}
          }
          node {
            seriesSeasonEpisode: series {
              ...$${SeriesSeasonEpisodeFragment.name}
            }
          }
        }
      }
    }
    principalCredits: principalCreditsV2(filter: { mode: "DEFAULT" }) {
      grouping {
        text
      }
      credits(limit: 5) {
        ...$${NameProfileCreditFragment.name}
      }
    }
    topCast: principalCreditsV2(filter: { mode: "TOP_CAST" }) {
      totalCredits
      grouping {
        groupingId
      }
      credits(limit: 10) {
        ...$${RoleCreditFragment.name}
      }
    }
    outline: plots(first: 1, filter: { type: OUTLINE }) {
      edges {
        node {
          plotText {
            plainText
          }
        }
      }
    }
    prestigiousAwardSummary {
      awardNomination {
        award {
          text
        }
      }
      wins
      nominations
    }
    awardWins: awardNominations(first: 0, filter: { wins: WINS_ONLY }) {
      total
    }
    awardNominations: awardNominations(
      first: 0
      filter: { wins: EXCLUDE_WINS }
    ) {
      total
    }
    productionBudget {
      budget {
        amount
        currency
      }
    }
  }
}

$${TitlePosterFragment.fragment}
$${NameProfileCreditFragment.fragment}
$${RoleCreditFragment.fragment}
$${SeriesSeasonEpisodeFragment.fragment}
"""
}

fun TitleDetailsQuery.Result.Title.toTitleDetails(): TitleDetails {
    return TitleDetails(
        id = MediaEntityId(id),
        name = titleText.text,
        poster = primaryImage?.url?.let(::transformImageUrl),
        contentRating = certificate?.rating,
        genre = titleGenres.genres.joinToString { it.genre.text },
        principalCreditsByGroup = principalCredits.associate { groupedCredits ->
            groupedCredits.grouping.text to groupedCredits.credits.map { it.name.toNameProfile() }
        },
        description = outline.edges.firstOrNull()?.node?.plotText?.plainText,
        yearReleased = run {
            val releaseYearDate = releaseYear?.year ?: return@run null

            if (episodes != null) {
                val endYear = releaseYear.endYear
                if (endYear == null) {
                    "${releaseYearDate}—"
                } else {
                    "${releaseYearDate}–${endYear}"
                }
            } else {
                releaseYearDate.toString()
            }
        },
        rating = ratingsSummary.aggregateRating?.toString()?.let { aggregate ->
            TitleDetails.Rating(
                value = aggregate,
                best = when (id) {
                    "tt0088258" -> "11" // "Well, it's one louder, isn't it?"
                    else -> "10"
                },
                count = ratingsSummary.voteCount?.toString(),
            )
        },
        duration = runtime?.displayableProperty?.value?.plainText,
        episodeMetadata = run {
            val series = series ?: return@run null
            val seriesSeasonEpisode = seriesSeasonEpisode ?: return@run null

            TitleDetails.EpisodeMetadata(
                seriesTitle = series.seriesTitle.titleText.text,
                season = seriesSeasonEpisode.displayableEpisodeNumber.displayableSeason.text,
                number = seriesSeasonEpisode.displayableEpisodeNumber.episodeNumber.text,
            )
        },
        genres = titleGenres.genres.map { it.genre.text },
        topCast = topCast.toTopCast(),
        topEpisodes = episodes?.toTopEpisodes(),
    )
}

private fun List<TitleDetailsQuery.Result.Title.TopCastCredits>.toTopCast(): TitleDetails.TopCast? {
    val topCastGrouping = firstOrNull() ?: return null

    return TitleDetails.TopCast(
        topCast = topCastGrouping.credits.map { castCredit -> castCredit.toCastCredit() },
        castTotal = topCastGrouping.totalCredits,
        hasMore = topCastGrouping.totalCredits > topCastGrouping.credits.size,
        groupingId = CreditGroupingId(topCastGrouping.grouping.groupingId),
    )
}

private fun TitleDetailsQuery.Result.Title.Episodes.toTopEpisodes(): TitleDetails.TopEpisodes {
    return TitleDetails.TopEpisodes(
        episodeCount = episodes.total,
        episodes = episodes.topEpisodes.map { topEpisode ->
            TitleDetails.TopEpisode(
                moviePoster = topEpisode.poster.toMoviePoster(),
                season = topEpisode.node.seriesSeasonEpisode.displayableEpisodeNumber.displayableSeason.text,
                number = topEpisode.node.seriesSeasonEpisode.displayableEpisodeNumber.episodeNumber.text,
            )
        }
    )
}
