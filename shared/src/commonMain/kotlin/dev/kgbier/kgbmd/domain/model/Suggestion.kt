package dev.kgbier.kgbmd.domain.model

data class Suggestion(
    val id: MediaEntityId,
    val title: String,
    val type: SearchSuggestionType?,
    val year: String?,
    val tidbit: String?,
    val thumbnailUrl: String?,
)

enum class SearchSuggestionType {
    Movie,      //  q: "feature", "TV movie"
    TvShow,     //  q: "TV mini-series", "TV series"
    CastOrCrew, // id: "nm0000000"
    Game,       //  q: ""
}

fun getSuggestionType(id: String, type: String?): SearchSuggestionType? = when (type) {
    "feature", "TV movie" -> SearchSuggestionType.Movie
    "TV mini-series", "TV series" -> SearchSuggestionType.TvShow
    "video game" -> SearchSuggestionType.Game
    else -> {
        if (id.startsWith("nm")) SearchSuggestionType.CastOrCrew
        else null
    }
}
