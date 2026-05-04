package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.TitleDetails
import kotlinx.serialization.Serializable

class TitleDetailsQuery : GraphqlQuery<TitleDetailsQuery.Params, TitleDetailsQuery.Result> {

    @Serializable
    data class Params(val id: String)

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(
            val titleText: TitleText,
            val originalTitleText: OriginalTitleText,
            val titleType: TitleType,
            val primaryImage: PrimaryImage?,
            val releaseYear: ReleaseYear,
            val certificate: Certificate?,
            val ratingsSummary: RatingsSummary,
            val runtime: Runtime?,
            val titleGenres: TitleGenres,
            val principalCredits: List<PrincipalCreditsForGrouping>,
            val topCast: List<TopCastCredits>,
            val outline: Plot,
        ) {
            @Serializable
            data class TitleText(val text: String)

            @Serializable
            data class OriginalTitleText(val text: String)

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
                val credits: List<Credit>,
            ) {
                @Serializable
                data class Grouping(
                    val groupingId: String,
                )

                @Serializable
                data class Credit(
                    val name: NameProfileCreditFragment,
                    val creditedRoles: CreditedRoles,
                ) {
                    @Serializable
                    data class CreditedRoles(val edges: List<Edge>) {
                        @Serializable
                        data class Edge(val node: Node) {
                            @Serializable
                            data class Node(val text: String?)
                        }
                    }
                }
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
    titleText {
      text
    }
    originalTitleText {
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
        ...$${NameProfileCreditFragment.name}
        creditedRoles(first: 10) {
          edges {
            node {
              text
            }
          }
        }
      }
    }
    outline: plots(first: 1, filter: { type: OUTLINE }) {
      edges {
        node {
          ...PlotData
        }
      }
    }
  }
}

$${NameProfileCreditFragment.fragment}

fragment PlotData on Plot {
  plotText {
    plainText
  }
}
"""
}

fun TitleDetailsQuery.Result.Title.toTitleDetails(): TitleDetails {
    return TitleDetails(
        name = titleText.text,
        poster = primaryImage?.url?.let(::transformImageUrl),
        contentRating = certificate?.rating,
        genre = titleGenres.genres.joinToString { it.genre.text },
        principalCreditsByGroup = principalCredits.associate { groupedCredits ->
            groupedCredits.grouping.text to groupedCredits.credits.map { it.name.toNameProfile() }
        },
        description = outline.edges.firstOrNull()?.node?.plotText?.plainText,
        yearReleased = releaseYear.year?.toString(),
        rating = ratingsSummary.aggregateRating?.toString()?.let { aggregate ->
            TitleDetails.Rating(
                value = aggregate,
                best = "10",
                count = ratingsSummary.voteCount?.toString(),
            )
        },
        duration = runtime?.displayableProperty?.value?.plainText,
        topCast = topCast.toTopCast(),
    )
}

private fun List<TitleDetailsQuery.Result.Title.TopCastCredits>.toTopCast(): TitleDetails.TopCast? {
    val topCastGrouping = firstOrNull() ?: return null

    return TitleDetails.TopCast(
        topCast = topCastGrouping.credits.map { castCredit ->
            TitleDetails.CastCredit(
                nameProfile = castCredit.name.toNameProfile(),
                roles = castCredit.creditedRoles.edges.mapNotNull { it.node.text },
            )
        },
        isMore = topCastGrouping.totalCredits > topCastGrouping.credits.size,
        groupingId = CreditGroupingId(topCastGrouping.grouping.groupingId),
    )
}
