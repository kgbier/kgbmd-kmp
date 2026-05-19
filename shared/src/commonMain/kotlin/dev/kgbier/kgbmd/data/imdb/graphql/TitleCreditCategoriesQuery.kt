package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import kotlinx.serialization.Serializable

class TitleCreditCategoriesQuery :
    GraphqlQuery<TitleCreditCategoriesQuery.Params, TitleCreditCategoriesQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val count: Int,
        val after: String? = null,
    )

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(val creditGroupings: CreditGroupings) {
            @Serializable
            data class CreditGroupings(
                val total: Int,
                val edges: List<Edge>,
            ) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(
                        val grouping: Grouping,
                        val credits: Credits,
                    ) {
                        @Serializable
                        data class Grouping(
                            val groupingId: String,
                            val text: String,
                        )

                        @Serializable
                        data class Credits(val total: Int)
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query TitleCreditCategories($id: ID!, $after: String, $count: Int!) {
  title(id: $id) {
    creditGroupings(first: $count, after: $after) {
      total
      edges {
        node {
          grouping {
            groupingId
            text
          }
          credits(first: 0) {
            total
          }
        }
      }
      pageInfo {
        ...$${PaginationFragment.fragment}
      }
    }
  }
}
$${PaginationFragment.fragment}
"""
}

fun TitleCreditCategoriesQuery.Result.Title.CreditGroupings.Edge.toCreditGrouping(
): CreditGrouping = CreditGrouping(
    id = CreditGroupingId(node.grouping.groupingId),
    name = node.grouping.text,
    count = node.credits.total,
)
