package dev.kgbier.kgbmd.data.imdb.graphql

import kotlinx.serialization.Serializable

@Serializable
data class SeriesSeasonEpisodeFragment(
    val displayableEpisodeNumber: DisplayableEpisodeNumber,
) {
    @Serializable
    data class DisplayableEpisodeNumber(
        val displayableSeason: DisplayableSeason,
        val episodeNumber: EpisodeNumber,
    ) {
        @Serializable
        data class DisplayableSeason(val text: String)

        @Serializable
        data class EpisodeNumber(val text: String)
    }

    companion object {
        const val name = "SeriesSeasonEpisodeFragment"
        const val fragment = """
fragment $name on Series {
  displayableEpisodeNumber {
    displayableSeason {
      text
    }
    episodeNumber {
      text
    }
  }
}
"""
    }
}
