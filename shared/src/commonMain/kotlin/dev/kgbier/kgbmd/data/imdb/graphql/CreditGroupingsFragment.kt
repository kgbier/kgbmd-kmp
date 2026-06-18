package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import kotlinx.serialization.Serializable

@Serializable
data class CreditGroupingsFragment(
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

    companion object {
        const val name = "CreditGroupings"
        const val fragment = """
fragment $name on CreditGroupingConnection {
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
    ...${PaginationFragment.name}
  }
}
"""
    }
}

fun CreditGroupingsFragment.Edge.toCreditGrouping(
): CreditGrouping = CreditGrouping(
    id = CreditGroupingId(node.grouping.groupingId),
    name = node.grouping.text,
    count = node.credits.total,
)
