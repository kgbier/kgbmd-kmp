package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.TitleCredit
import kotlinx.serialization.Serializable

object NameCreditsQuery : GraphqlQuery<NameCreditsQuery.Params, NameCreditsQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val groupingId: String,
        val count: Int,
    )

    @Serializable
    data class Result(val name: Name) {
        @Serializable
        data class Name(val creditGroupings: CreditGroupings) {
            @Serializable
            data class CreditGroupings(val edges: List<Edge>) {
                @Serializable
                data class Edge(val node: Node) {
                    @Serializable
                    data class Node(val credits: Credits) {
                        @Serializable
                        data class Credits(val edges: List<Edge>) {
                            @Serializable
                            data class Edge(
                                val roleCredit: RoleCreditFragment,
                                val node: Node,
                            ) {
                                @Serializable
                                data class Node(
                                    val titlePoster: TitlePosterFragment,
                                    val titleInfo: TitleInfo,
                                ) {
                                    @Serializable
                                    data class TitleInfo(
                                        val releaseYear: ReleaseYear?,
                                        val productionStatus: ProductionStatus?,
                                    ) {
                                        @Serializable
                                        data class ReleaseYear(
                                            val year: String?,
                                        )

                                        @Serializable
                                        data class ProductionStatus(
                                            val currentProductionStage: CurrentProductionStage?,
                                        ) {
                                            @Serializable
                                            data class CurrentProductionStage(
                                                val id: String,
                                                val text: String,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query NameCredits($id: ID!, $groupingId: ID!, $count: Int!) {
  name(id: $id) {
    creditGroupings(first: 1, filter: { creditLevelFilter: { groupings: [$groupingId] } }) {
      edges {
        node {
          credits(first: $count) {
            edges {
              roleCredit: node {
                ...$${RoleCreditFragment.name}
              }
              node {
                titlePoster: title {
                  ...$${TitlePosterFragment.name}
                }
                titleInfo: title {
                  releaseYear {
                    year
                  }
                  productionStatus {
                    currentProductionStage {
                      id
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
  }
}
$${TitlePosterFragment.fragment}
$${RoleCreditFragment.fragment}
$${NameProfileCreditFragment.fragment}
"""
}

fun NameCreditsQuery.Result.Name.CreditGroupings.Edge.Node.Credits.Edge.toTitleCredit(
) = TitleCredit(
    moviePoster = node.titlePoster.toMoviePoster(),
    year = node.titleInfo.releaseYear?.year,
    productionStatus = run {
        val a = node.titleInfo.productionStatus?.currentProductionStage
        when (a?.id) {
            "released" -> null
            else -> a?.text
        }
    },
    roles = roleCredit.toCastCredit().roles,
)
