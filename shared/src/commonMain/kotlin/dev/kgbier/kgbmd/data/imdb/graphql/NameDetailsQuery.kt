package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.NameDetails
import kotlinx.serialization.Serializable

class NameDetailsQuery : GraphqlQuery<NameDetailsQuery.Params, NameDetailsQuery.Result> {

    @Serializable
    data class Params(val id: String)

    @Serializable
    data class Result(val name: Name) {
        @Serializable
        data class Name(
            val nameText: NameText,
            val bio: Bio,
            val primaryImage: PrimaryImage?,
            val knownForV2: KnownFor,
        ) {
            @Serializable
            data class NameText(val text: String)

            @Serializable
            data class Bio(val text: Text) {
                @Serializable
                data class Text(val plainText: String)
            }

            @Serializable
            data class PrimaryImage(val url: String?)

            @Serializable
            data class KnownFor(val credits: List<Credit>) {
                @Serializable
                data class Credit(
                    val title: Title,
                    val creditedRoles: CreditedRoles,
                ) {
                    @Serializable
                    data class Title(
                        val id: String,
                        val titleText: TitleText,
                        val primaryImage: PrimaryImage?,
                        val releaseYear: ReleaseYear?,
                        val ratingsSummary: RatingsSummary?,
                    ) {
                        @Serializable
                        data class TitleText(val text: String)

                        @Serializable
                        data class ReleaseYear(val year: Int, val endYear: Int?)

                        @Serializable
                        data class RatingsSummary(val aggregateRating: String?)
                    }

                    @Serializable
                    data class CreditedRoles(val edges: List<Edge>) {
                        @Serializable
                        data class Edge(val node: Node) {
                            @Serializable
                            data class Node(val text: String?, val category: Category) {
                                @Serializable
                                data class Category(val categoryId: String, val text: String)
                            }
                        }
                    }
                }
            }
        }
    }

    override val document: String = $$"""
query NameDetails($id: ID!) {
  name(id: $id) {
    nameText {
      text
    }
    bio {
      text {
        plainText
      }
    }
    primaryImage {
      url
    }
    knownForV2(limit: 10) {
      credits {
        title {
          id
          titleText {
            text
          }
          primaryImage {
            url
          }
          ratingsSummary {
            aggregateRating
          }
        }
        creditedRoles(first: 1) {
          edges {
            node {
              category {
                categoryId
                text
              }
              text
            }
          }
        }
      }
    }
  }
}

"""
}

fun NameDetailsQuery.Result.Name.toNameDetails() = NameDetails(
    name = nameText.text,
    headshot = primaryImage?.url?.let(::transformImageUrl),
    description = bio.text.plainText,
    topCredits = knownForV2.credits.map { credit ->
        NameDetails.TopCredit(
            id = MediaEntityId(credit.title.id),
            name = credit.title.titleText.text,
            year = credit.title.releaseYear?.year?.toString(),
            poster = credit.title.primaryImage?.url?.let(::transformImageUrl),
            rating = credit.title.ratingsSummary?.aggregateRating,
            role = credit.creditedRoles.edges.first().let { edge ->
                val role = edge.node
                role.text?.replaceFirstChar(Char::uppercaseChar)
                    ?: role.category.text
            },
        )
    },
)
