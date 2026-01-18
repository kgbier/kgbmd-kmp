package dev.kgbier.kgbmd.data.imdb

import dev.kgbier.kgbmd.data.imdb.graphql.GraphqlQuery
import dev.kgbier.kgbmd.data.imdb.graphql.MostPopularListQuery
import dev.kgbier.kgbmd.data.imdb.graphql.NameDetailsQuery
import dev.kgbier.kgbmd.data.imdb.graphql.TitleDetailsQuery
import dev.kgbier.kgbmd.data.imdb.model.RatingResponse
import dev.kgbier.kgbmd.data.imdb.model.SuggestionResponse
import dev.kgbier.kgbmd.data.operation.JsonP
import dev.kgbier.kgbmd.service.Services
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.buildUrl
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.set
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object ImdbService {

    private const val graphqEndpoint = "https://api.graphql.imdb.com/"

    // https://v2.sg.media-imdb.com/suggestion/d/dark_knight.json
    private fun buildSuggestionUrl(query: String) = buildUrl {
        set(
            scheme = "https",
            host = "v2.sg.media-imdb.com",
        )
        path(
            "suggestion",
            query.first().toString(),
            "$query.json",
        )
    }

    // https://p.media-imdb.com/static-content/documents/v1/title/tt0468569/ratings%3Fjsonp=imdb.rating.run:imdb.api.title.ratings/data.json
    private fun buildRatingUrl(ttid: String) = buildUrl {
        set(
            scheme = "https",
            host = "p.media-imdb.com"
        )
        path(
            "static-content/documents/v1/title",
            ttid,
            "ratings?jsonp=imdb.rating.run:imdb.api.title.ratings",
            "data.json",
        )
    }

    private suspend fun getHotList(
        type: MostPopularListQuery.Params.ChartTitleType,
    ) = graphqlQuery(
        query = MostPopularListQuery(),
        params = MostPopularListQuery.Params(count = 100, type = type),
    )

    suspend fun getHotMovies() =
        getHotList(MostPopularListQuery.Params.ChartTitleType.MOST_POPULAR_MOVIES)

    suspend fun getHotShows() =
        getHotList(MostPopularListQuery.Params.ChartTitleType.MOST_POPULAR_TV_SHOWS)

    suspend fun search(query: String): SuggestionResponse? {
        val validatedQuery = query
            .trim()
            .lowercase()
            .replace(" ", "_")

        if (validatedQuery.isEmpty()) return null

        val url = buildSuggestionUrl(validatedQuery)

        val response = Services.ktor.get(url)

        return json.decodeFromString(response.bodyAsText())
    }

    suspend fun getRating(ttid: String): RatingResponse? {
        val url = buildRatingUrl(ttid)

        val response = Services.ktor.get(url)

        val validatedJson = JsonP.toJson(response.bodyAsText()) ?: return null
        return json.decodeFromString(validatedJson)
    }

    suspend fun getTitleDetails(ttid: String) =
        graphqlQuery(TitleDetailsQuery(), TitleDetailsQuery.Params(ttid))

    suspend fun getNameDetails(nmid: String) =
        graphqlQuery(NameDetailsQuery(), NameDetailsQuery.Params(nmid))

    private suspend inline fun <reified Params, reified Result> graphqlQuery(
        query: GraphqlQuery<Params, Result>,
        params: Params?,
    ): Result = graphqlQuery(
        query = query,
        params = params,
        requestSerializer = serializer<GraphqlRequest<Params>>(),
        resultSerializer = serializer<GraphqlResponse<Result>>(),
    )

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Serializable
    data class GraphqlResponse<Result>(val data: Result)

    @Serializable
    data class GraphqlRequest<Variables>(val variables: Variables?, val query: String)

    private suspend fun <Params, Result> graphqlQuery(
        query: GraphqlQuery<Params, Result>,
        params: Params?,
        requestSerializer: KSerializer<GraphqlRequest<Params>>,
        resultSerializer: KSerializer<GraphqlResponse<Result>>,
    ): Result {
        val graphqlRequest = GraphqlRequest(
            query = query.document
                .replace('\n', ' ')
                .replace("  ", ""),
            variables = params,
        )
        val bodyText = Json.encodeToString(
            serializer = requestSerializer,
            value = graphqlRequest,
        )

        val response = Services.ktor.post(graphqEndpoint) {
            contentType(ContentType.Application.Json)
            setBody(bodyText)
        }
        return json.decodeFromString(resultSerializer, response.bodyAsText())
            .data
    }
}
