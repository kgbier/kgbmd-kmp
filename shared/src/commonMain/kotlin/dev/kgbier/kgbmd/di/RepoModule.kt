package dev.kgbier.kgbmd.di

import dev.kgbier.kgbmd.data.repo.ImdbMediaInfoRepo

class RepoModule(
    private val serviceModule: ServiceModule,
) {
    val mediaInfoRepo by lazy {
        ImdbMediaInfoRepo(
            imdbService = serviceModule.imdbService,
        )
    }
}
