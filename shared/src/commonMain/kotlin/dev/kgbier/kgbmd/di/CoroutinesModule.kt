package dev.kgbier.kgbmd.di

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CoroutinesModule {
    val rootContext = Dispatchers.Default
        .plus(Job())
        .plus(CoroutineExceptionHandler { context, throwable ->
            println("Fatal exception in $context: $throwable")
            throw throwable
        })

    val rootScope = CoroutineScope(rootContext)
}
