package dev.kgbier.kgbmd.ui.di

import co.touchlab.kermit.Severity
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.NullRequestDataException
import coil3.request.crossfade
import coil3.util.Logger
import co.touchlab.kermit.Logger.Companion as KLogger

object CoilModule {
    fun createImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .crossfade(true)
        .logger(CoilDebugLogger())
        .build()
}

class CoilDebugLogger(
    override var minLevel: Logger.Level = Logger.Level.Verbose
) : Logger {

    override fun log(
        tag: String,
        level: Logger.Level,
        message: String?,
        throwable: Throwable?
    ) {
        // NullRequestDataException isn't an error, it may occur when an image is
        // intentionally unavailable and presenting the fallback state is desired.
        if (throwable is NullRequestDataException) {
            KLogger.d(
                throwable = null,
                tag = tag,
                messageString = message ?: ""
            )
        } else {
            KLogger.log(
                severity = level.toKermitSeverity(),
                throwable = throwable,
                tag = tag,
                message = message ?: ""
            )
        }
    }
}

private fun Logger.Level.toKermitSeverity(): Severity = when (this) {
    Logger.Level.Verbose -> Severity.Verbose
    Logger.Level.Debug -> Severity.Debug
    Logger.Level.Info -> Severity.Info
    Logger.Level.Warn -> Severity.Warn
    Logger.Level.Error -> Severity.Error
}
