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
            val id: String,
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
                    val poster: TitlePosterFragment,
                    val creditedRoles: CreditedRoles,
                ) {
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
    id
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
        poster: title {
          ...$${TitlePosterFragment.name}
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
$${TitlePosterFragment.fragment}
"""
}

fun NameDetailsQuery.Result.Name.toNameDetails() = NameDetails(
    id = MediaEntityId(id),
    name = nameText.text,
    headshot = primaryImage?.url?.let(::transformImageUrl),
    description = bio.text.plainText,
    topCredits = knownForV2.credits.map { credit ->
        NameDetails.TopCredit(
            poster = credit.poster.toMoviePoster(),
            role = credit.creditedRoles.edges.first().let { edge ->
                val role = edge.node
                role.text?.replaceFirstChar(Char::uppercaseChar)
                    ?: role.category.text
            },
        )
    },
)
