package dev.kgbier.kgbmd.domain.repo

import dev.kgbier.kgbmd.domain.model.MediaEntityDetails
import dev.kgbier.kgbmd.domain.model.MediaEntityId
import dev.kgbier.kgbmd.domain.model.MoviePoster
import dev.kgbier.kgbmd.domain.model.Suggestion

interface MediaInfoRepo {
    suspend fun getMovieHotListPosters(): List<MoviePoster>
    suspend fun getTvShowHotListPosters(): List<MoviePoster>
    suspend fun getSearchResults(query: String): List<Suggestion>
    suspend fun getRating(id: MediaEntityId): String?
    suspend fun getMediaEntityDetails(id: MediaEntityId): MediaEntityDetails?
}
