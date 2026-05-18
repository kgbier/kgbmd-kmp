package dev.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

class MostPopularListQuery :
    GraphqlQuery<MostPopularListQuery.Params, MostPopularListQuery.Result> {

    @Serializable
    data class Params(
        val count: Int,
        val type: ChartTitleType,
        val after: String? = null,
    ) {
        @Serializable
        enum class ChartTitleType {
            MOST_POPULAR_TV_SHOWS,
            MOST_POPULAR_MOVIES,
        }
    }

    @Serializable
    data class Result(val chartTitles: ChartTitles) {
        @Serializable
        data class ChartTitles(
            val total: Int,
            val pageInfo: PaginationFragment,
            val edges: List<Edge>,
        ) {
            @Serializable
            data class Edge(
                val currentRank: Int,
                val poster: TitlePosterFragment
            )
        }
    }

    override val document: String = $$"""
query MostPopularList($count: Int!, $type: ChartTitleType!, $after: String) {
  chartTitles(first: $count, after: $after, chart: { chartType: $type }) {
    total
    pageInfo {
      ...$${PaginationFragment.name}
    }
    edges {
      currentRank
      poster: node {
        ...$${TitlePosterFragment.name}
      }
    }
  }
}
$${PaginationFragment.fragment}
$${TitlePosterFragment.fragment}
"""
}
