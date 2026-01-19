package dev.kgbier.kgbmd.di

import dev.kgbier.kgbmd.data.imdb.KtorImdbService

class ServiceModule(
    private val ktorModule: KtorModule,
) {
    val imdbService by lazy {
        KtorImdbService(
            ktor = ktorModule.ktor,
        )
    }
}
