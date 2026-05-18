package dev.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

@Serializable
data class PaginationFragment(
    val hasNextPage: Boolean,
    val endCursor: String,
) {
    companion object {
        const val name = "Pagination"
        const val fragment = """
fragment $name on PageInfo {
  hasNextPage
  endCursor
}
"""
    }
}
