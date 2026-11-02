package dev.kgbier.kgbmd.di

class CoreModule(
    val platformModule: CorePlatformModule,
) {
    val coroutinesModule = CoroutinesModule()
}

expect class CorePlatformModule
