package dev.kgbier.kgbmd.data.repo

import dev.kgbier.kgbmd.data.imdb.ImdbService
import dev.kgbier.kgbmd.data.imdb.graphql.MostPopularListQuery
import dev.kgbier.kgbmd.data.imdb.graphql.toCastCredit
import dev.kgbier.kgbmd.data.imdb.graphql.toCreditGrouping
import dev.kgbier.kgbmd.data.imdb.graphql.toMoviePoster
import dev.kgbier.kgbmd.data.imdb.graphql.toNameDetails
import dev.kgbier.kgbmd.data.imdb.graphql.toTitleCredit
import dev.kgbier.kgbmd.data.imdb.graphql.toTitleDetails
import dev.kgbier.kgbmd.data.imdb.model.transform
import dev.kgbier.kgbmd.domain.model.CastCredit
import dev.kgbier.kgbmd.domain.model.CreditGrouping
import dev.kgbier.kgbmd.domain.model.CreditGroupingId
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.domain.model.TitleCredit
import dev.kgbier.kgbmd.domain.model.transformRatingResponse
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo

class ImdbMediaInfoRepo(
    private val imdbService: ImdbService,
) : MediaInfoRepo {

    override suspend fun getMovieHotListPosters(page: String?): List<MoviePoster> =
        imdbService.getHotMovies(page).transform()

    override suspend fun getTvShowHotListPosters(page: String?): List<MoviePoster> =
        imdbService.getHotShows(page).transform()

    override suspend fun getSearchResults(query: String): List<Suggestion> =
        imdbService.search(query)?.transform() ?: emptyList()

    override suspend fun getRating(id: MediaEntityId): String? =
        imdbService.getRating(id)?.run(::transformRatingResponse)

    override suspend fun getMediaEntityDetails(id: MediaEntityId): MediaEntityDetails? = when {
        id.id.startsWith(NAME_ID_PREFIX) -> imdbService.getNameDetails(id).name.toNameDetails()
        id.id.startsWith(TITLE_ID_PREFIX) -> imdbService.getTitleDetails(id).title.toTitleDetails()
        else -> null
    }

    override suspend fun getCreditGroupsForTitle(id: MediaEntityId): List<CreditGrouping> =
        imdbService.getTitleCreditCategories(id).title.creditGroupings.edges.map { it.toCreditGrouping() }

    override suspend fun getCreditGroupsForName(id: MediaEntityId): List<CreditGrouping> =
        imdbService.getNameCreditCategories(id).name.creditGroupings.edges.map { it.toCreditGrouping() }

    override suspend fun getCreditsForTitleGroup(
        id: MediaEntityId,
        groupingId: CreditGroupingId,
    ): List<CastCredit> = imdbService.getTitleCredits(id, groupingId)
        .title.creditGroupings.edges.firstOrNull()?.node?.credits?.edges
        ?.map { it.node.toCastCredit() } ?: emptyList()

    override suspend fun getCreditsForNameGroup(
        id: MediaEntityId,
        groupingId: CreditGroupingId,
    ): List<TitleCredit> = imdbService.getNameCredits(id, groupingId)
        .name.creditGroupings.edges.firstOrNull()?.node?.credits?.edges
        ?.sortedBy {
            // Some unreleased items sort at the top, some at the bottom.
            // Probably needs to be solved by bucketing broadly into
            // released/unreleased sub-categories/groups to support pagination
            when (it.node.titleInfo.productionStatus?.currentProductionStage?.id) {
                "released" -> 1
                else -> 0
            }
        }?.map { it.toTitleCredit() } ?: emptyList()

    fun MostPopularListQuery.Result.transform() =
        chartTitles.edges.map { it.poster.toMoviePoster() }

    companion object {
        private const val NAME_ID_PREFIX = "nm"
        private const val TITLE_ID_PREFIX = "tt"
    }
}
