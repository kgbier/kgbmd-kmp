package dev.kgbier.kgbmd.di

import dev.kgbier.kgbmd.data.imdb.KtorImdbService
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

class ServiceModule(
    private val ktorModule: KtorModule,
) {
    val imdbService by lazy {
        KtorImdbService(
            ktor = ktorModule.ktor.config {
                defaultRequest {
                    headers {
                        header("X-Imdb-Client-Name", "IMDbConsumerDesktop-ConsentBanner")
                    }
                }
            },
        )
    }
}
