package dev.kgbier.kgbmd.data.imdb.graphql

import dev.kgbier.kgbmd.data.imdb.model.transformImageUrl
import dev.kgbier.kgbmd.domain.model.TitleDetails
import kotlinx.serialization.Serializable

@Serializable
data class WatchOptionFragment(
    val title: ValueString,
    val description: ValueString?,
    val provider: Provider,
) {

    @Serializable
    data class ValueString(
        val value: String,
    )

    @Serializable
    data class Provider(
        val watchOptionCategoryType: WatchOptionCategoryType?,
        val logos: Logos?,
    ) {
        @Serializable
        data class Logos(
            val slate: Slate?,
        ) {
            @Serializable
            data class Slate(
                val url: String?,
            )
        }
    }

    companion object {
        const val name = "WatchOptionFragment"
        const val fragment = """
fragment $name on WatchOption {
  title {
    value
  }
  description {
    value
  }
  provider {
    watchOptionCategoryType
    logos {
      slate {
        url
      }
    }
  }
}
"""
    }
}

fun WatchOptionFragment.toWatchOption(
): TitleDetails.WatchOption = when (provider.watchOptionCategoryType) {
    WatchOptionCategoryType.THEATRICAL -> TitleDetails.WatchOption.Cinema

    WatchOptionCategoryType.IMDB_TV,
    WatchOptionCategoryType.PHYSICAL,
    WatchOptionCategoryType.PODCAST,
    WatchOptionCategoryType.RENT_OR_BUY,
    WatchOptionCategoryType.SUBSCRIPTION,
    null,
        -> TitleDetails.WatchOption.Watchable(
        title = title.value,
        description = description?.value,
        image = provider.logos?.slate?.url?.let { transformImageUrl(it) },
    )
}
