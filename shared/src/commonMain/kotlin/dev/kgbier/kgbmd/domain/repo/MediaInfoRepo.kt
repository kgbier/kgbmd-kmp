package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.domain.model.TitleCredit
import dev.kgbier.kgbmd.domain.model.TitleEpisode
import dev.kgbier.kgbmd.domain.model.TitleSeason
import dev.kgbier.kgbmd.domain.model.TitleSeasonId

interface MediaInfoRepo {
    suspend fun getMovieHotListPosters(page: String?): List<MoviePoster>
    suspend fun getTvShowHotListPosters(page: String?): List<MoviePoster>
    suspend fun getSearchResults(query: String): List<Suggestion>
    suspend fun getRating(id: MediaEntityId): String?
    suspend fun getMediaEntityDetails(id: MediaEntityId): MediaEntityDetails?

    suspend fun getCreditGroupsForTitle(id: MediaEntityId): List<CreditGrouping>
    suspend fun getCreditGroupsForName(id: MediaEntityId): List<CreditGrouping>
    suspend fun getEpisodeSeasonsForTitle(id: MediaEntityId): List<TitleSeason>

    suspend fun getCreditsForTitleGroup(
        id: MediaEntityId,
        groupingId: CreditGroupingId,
    ): List<CastCredit>

    suspend fun getCreditsForNameGroup(
        id: MediaEntityId,
        groupingId: CreditGroupingId,
    ): List<TitleCredit>

    suspend fun getEpisodesForTitleSeason(
        id: MediaEntityId,
        groupingId: TitleSeasonId,
    ): List<TitleEpisode>
}
