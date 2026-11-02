package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.TitleSeason
import dev.kgbier.kgbmd.domain.model.TitleSeasonId
import kotlinx.serialization.Serializable

object TitleEpisodeCategoriesQuery :
    GraphqlQuery<TitleEpisodeCategoriesQuery.Params, TitleEpisodeCategoriesQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val count: Int,
        val after: String? = null,
    )

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(val episodes: Episodes) {
            @Serializable
            data class Episodes(val displayableSeasons: DisplayableSeasons) {
                @Serializable
                data class DisplayableSeasons(val edges: List<Edge>) {
                    @Serializable
                    data class Edge(val node: Node) {
                        @Serializable
                        data class Node(
                            val season: String,
                            val text: String,
                        )
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query TitleEpisodeCategories($id: ID!, $count: Int!) {
  title(id: $id) {
    episodes {
      displayableSeasons(first: $count) {
        edges {
          node {
            season
            text
          }
        }
      }
    }
  }
}
"""
}

fun TitleEpisodeCategoriesQuery.Result.Title.Episodes.DisplayableSeasons.Edge.toTitleSeason(
): TitleSeason = TitleSeason(
    id = TitleSeasonId(node.season),
    name = node.text,
)
