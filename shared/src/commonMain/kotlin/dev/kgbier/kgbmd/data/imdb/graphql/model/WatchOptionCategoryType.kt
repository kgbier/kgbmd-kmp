package dev.kgbier.kgbmd.data.imdb.graphql.model

import kotlinx.serialization.Serializable

@Serializable
enum class WatchOptionCategoryType {
    IMDB_TV,
    PHYSICAL,
    PODCAST,
    RENT_OR_BUY,
    SUBSCRIPTION,
    THEATRICAL,
}
