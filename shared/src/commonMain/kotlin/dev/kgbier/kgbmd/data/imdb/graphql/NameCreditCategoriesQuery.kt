package dev.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

object NameCreditCategoriesQuery :
    GraphqlQuery<NameCreditCategoriesQuery.Params, NameCreditCategoriesQuery.Result> {

    @Serializable
    data class Params(
        val id: String,
        val count: Int,
        val after: String? = null,
    )

    @Serializable
    data class Result(val name: Name) {
        @Serializable
        data class Name(val creditGroupings: CreditGroupingsFragment)
    }

    override val document: String = $$"""
query NameCreditCategories($id: ID!, $after: String, $count: Int!) {
  name(id: $id) {
    creditGroupings(first: $count, after: $after) {
      ...$${CreditGroupingsFragment.name}
    }
  }
}
$${PaginationFragment.fragment}
$${CreditGroupingsFragment.fragment}
"""
}
