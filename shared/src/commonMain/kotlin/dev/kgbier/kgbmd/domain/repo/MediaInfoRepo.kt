package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion

interface MediaInfoRepo {
    suspend fun getMovieHotListPosters(page: String?): List<MoviePoster>
    suspend fun getTvShowHotListPosters(page: String?): List<MoviePoster>
    suspend fun getSearchResults(query: String): List<Suggestion>
    suspend fun getRating(id: MediaEntityId): String?
    suspend fun getMediaEntityDetails(id: MediaEntityId): MediaEntityDetails?

    suspend fun getCreditGroupsForTitle(id: MediaEntityId): List<CreditGrouping>
//    suspend fun getCreditGroupsForName(id: MediaEntityId): List<CreditGrouping>
//
//    suspend fun getCastCrewForCreditGroup(id: CreditGroupingId): List<CreditGrouping>
//    suspend fun getTitlesForCreditGroup(id: CreditGroupingId): List<CreditGrouping>
}
