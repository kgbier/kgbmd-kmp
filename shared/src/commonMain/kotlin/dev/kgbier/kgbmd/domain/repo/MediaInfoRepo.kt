package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.data.imdb.ImdbService
import dev.kgbier.kgbmd.data.imdb.graphql.MostPopularListQuery
import dev.kgbier.kgbmd.data.imdb.graphql.toMoviePoster
import dev.kgbier.kgbmd.data.imdb.graphql.toNameDetails
import dev.kgbier.kgbmd.data.imdb.graphql.toTitleDetails
import dev.kgbier.kgbmd.data.imdb.model.transformSuggestionResponse
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.domain.model.transformRatingResponse

object MediaInfoRepo {
    private const val NAME_ID_PREFIX = "nm"
    private const val TITLE_ID_PREFIX = "tt"

    suspend fun getMovieHotListPosters(): List<MoviePoster> =
        ImdbService.getHotMovies().transform()

    suspend fun getTvShowHotListPosters(): List<MoviePoster> =
        ImdbService.getHotShows().transform()

    suspend fun getSearchResults(query: String): List<Suggestion> =
        ImdbService.search(query).run(::transformSuggestionResponse)

    suspend fun getRating(id: String): String? =
        ImdbService.getRating(id)?.run(::transformRatingResponse)

    suspend fun getMediaEntityDetails(id: String): MediaEntityDetails? = when {
        id.startsWith(NAME_ID_PREFIX) -> ImdbService.getNameDetails(id).name.toNameDetails()
        id.startsWith(TITLE_ID_PREFIX) -> ImdbService.getTitleDetails(id).title.toTitleDetails()
        else -> null
    }

    fun MostPopularListQuery.Result.transform() = chartTitles.edges.map { it.toMoviePoster() }
}