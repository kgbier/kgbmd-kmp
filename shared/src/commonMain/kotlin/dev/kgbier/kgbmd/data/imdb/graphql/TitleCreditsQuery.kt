package dev.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

object TitleCreditsQuery : GraphqlQuery<TitleCreditsQuery.Params, TitleCreditsQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val groupingId: String,
        val count: Int,
    )

    @Serializable
    data class Result(val title: Title) {
        @Serializable
        data class Title(val creditGroupings: CreditGroupings) {
            @Serializable
            data class CreditGroupings(val edges: List<Edge>) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(val credits: Credits) {
                        @Serializable
                        data class Credits(val edges: List<Edge>) {
                            @Serializable
                            data class Edge(val node: RoleCreditFragment)
                        }
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query TitleCredits($id: ID!, $groupingId: ID!, $count: Int!) {
  title(id: $id) {
    creditGroupings(first: 1, filter: { creditLevelFilter: { groupings: [$groupingId] } }) {
      edges {
        node {
          credits(first: $count) {
            edges {
              node {
                ...$${RoleCreditFragment.name}
              }
            }
          }
        }
      }
    }
  }
}
$${RoleCreditFragment.fragment}
$${NameProfileCreditFragment.fragment}
"""
}
