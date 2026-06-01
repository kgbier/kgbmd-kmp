package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.domain.model.CastCredit
import kotlinx.serialization.Serializable

@Serializable
data class RoleCreditFragment(
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

    companion object {
        const val name = "RoleCreditFragment"
        const val fragment = """
fragment $name on CreditV2 {
  ...${NameProfileCreditFragment.name}
  creditedRoles(first: 10) {
    edges {
      node {
        text
      }
    }
  }
}
"""
    }
}

fun RoleCreditFragment.toCastCredit(): CastCredit = CastCredit(
    nameProfile = name.toNameProfile(),
    roles = creditedRoles.edges.mapNotNull { it.node.text },
)
