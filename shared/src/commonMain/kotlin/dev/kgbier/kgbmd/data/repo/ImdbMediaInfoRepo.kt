package dev.kgbier.kgbmd.data.repo

import dev.kgbier.kgbmd.data.imdb.ImdbService
import dev.kgbier.kgbmd.data.imdb.graphql.MostPopularListQuery
import dev.kgbier.kgbmd.data.imdb.graphql.toMoviePoster
import dev.kgbier.kgbmd.data.imdb.graphql.toNameDetails
import dev.kgbier.kgbmd.data.imdb.graphql.toTitleDetails
import dev.kgbier.kgbmd.data.imdb.model.transform
import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion
import dev.kgbier.kgbmd.domain.model.transformRatingResponse
import dev.kgbier.kgbmd.domain.repo.MediaInfoRepo

class ImdbMediaInfoRepo(
    private val imdbService: ImdbService,
) : MediaInfoRepo {

    override suspend fun getMovieHotListPosters(): List<MoviePoster> =
        imdbService.getHotMovies().transform()

    override suspend fun getTvShowHotListPosters(): List<MoviePoster> =
        imdbService.getHotShows().transform()

    override suspend fun getSearchResults(query: String): List<Suggestion> =
        imdbService.search(query)?.transform() ?: emptyList()

    override suspend fun getRating(id: MediaEntityId): String? =
        imdbService.getRating(id)?.run(::transformRatingResponse)

    override suspend fun getMediaEntityDetails(id: MediaEntityId): MediaEntityDetails? = when {
        id.id.startsWith(NAME_ID_PREFIX) -> imdbService.getNameDetails(id).name.toNameDetails()
        id.id.startsWith(TITLE_ID_PREFIX) -> imdbService.getTitleDetails(id).title.toTitleDetails()
        else -> null
    }

    fun MostPopularListQuery.Result.transform() = chartTitles.edges.map { it.toMoviePoster() }

    companion object {
        private const val NAME_ID_PREFIX = "nm"
        private const val TITLE_ID_PREFIX = "tt"
    }
}