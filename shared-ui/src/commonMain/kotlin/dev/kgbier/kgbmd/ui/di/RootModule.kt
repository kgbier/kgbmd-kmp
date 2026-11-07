package dev.kgbier.kgbmd.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.kgbier.kgbmd.di.CoreModule
import dev.kgbier.kgbmd.di.KtorModule
import dev.kgbier.kgbmd.di.RepoModule
import dev.kgbier.kgbmd.di.ServiceModule
import dev.kgbier.kgbmd.di.StorageModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.job

@Composable
expect fun rememberRootModule(): RootModule

fun CoroutineScope.createChild(): CoroutineScope =
    CoroutineScope(coroutineContext + Job(parent = coroutineContext.job))

class RootModule(
    val coreModule: CoreModule,
) {
    val coroutinesModule = coreModule.coroutinesModule
    val storageModule = StorageModule(coreModule)
    val ktorModule = KtorModule()
    val serviceModule = ServiceModule(ktorModule)
    val repoModule = RepoModule(
        serviceModule = serviceModule,
        storageModule = storageModule,
    )
}

val LocalRootModule =
    staticCompositionLocalOf<RootModule> { error("RootModule must be provided") }
