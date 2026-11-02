package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.TitleEpisode
import kotlinx.serialization.Serializable

object TitleEpisodesQuery
    : GraphqlQuery<TitleEpisodesQuery.Params, TitleEpisodesQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val groupingIds: List<String>,
        val count: Int,
    )

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(val episodes: Episodes) {
            @Serializable
            data class Episodes(val episodes: EpisodesQuery) {
                @Serializable
                data class EpisodesQuery(val edges: List<Edge>) {
                    @Serializable
                    data class Edge(
                        val titlePoster: TitlePosterFragment,
                        val titleEpisode: TitleEpisode,
                    ) {
                        @Serializable
                        data class TitleEpisode(
                            val series: Series?,
                            val releaseDate: ReleaseDate?,
                        ) {
                            @Serializable
                            data class Series(val displayableEpisodeNumber: DisplayableEpisodeNumber) {
                                @Serializable
                                data class DisplayableEpisodeNumber(val episodeNumber: EpisodeNumber) {
                                    @Serializable
                                    data class EpisodeNumber(val text: String)
                                }
                            }
                            @Serializable
                            data class ReleaseDate(val displayableProperty: DisplayableProperty) {
                                @Serializable
                                data class DisplayableProperty(
                                    val value: Value,
                                ) {
                                    @Serializable
                                    data class Value(val plainText: String)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query TitleEpisodeCategories($id: ID!, $count: Int!, $groupingIds: [String!]) {
  title(id: $id) {
    episodes {
      episodes(first: $count, filter: { includeSeasons: $groupingIds }) {
        edges {
          titlePoster: node {
            ...$${TitlePosterFragment.name}
          }
          titleEpisode: node {
            releaseDate {
              displayableProperty {
                value {
                  plainText
                }
              }
            }
            series {
              displayableEpisodeNumber {
                episodeNumber {
                  text
                }
              }
            }
          }
        }
      }
    }
  }
}
$${TitlePosterFragment.fragment}
"""
}

fun TitleEpisodesQuery.Result.Title.Episodes.EpisodesQuery.Edge.toTitleEpisode(
): TitleEpisode = TitleEpisode(
    moviePoster = titlePoster.toMoviePoster(),
    episodeNumber = titleEpisode.series?.displayableEpisodeNumber?.episodeNumber?.text,
    releaseDate = titleEpisode.releaseDate?.displayableProperty?.value?.plainText,
)
