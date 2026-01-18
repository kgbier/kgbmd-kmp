package dev.kgbier.kgbmd.util.logger

import dev.kgbier.kgbmd.util.logger.Logger.Level

inline fun Logger.logCheckingUnconfinedSeverity(
    level: Level,
    tag: String? = null,
    isRequired: Boolean = false,
    error: Throwable? = null,
    noinline messageProvider: () -> String,
    unconfinedLoggingSeverity: Boolean = true,
) {
    val minimumLoggingSeverity = when (unconfinedLoggingSeverity) {
        true -> Level.Verbose
        false -> Level.Warning
    }

    if (isRequired || level isAtLeast minimumLoggingSeverity) {
        log(level, tag, error, messageProvider)
    }
}

inline infix fun Level.isAtLeast(minimum: Level): Boolean = ordinal >= minimum.ordinal

inline fun Logger.v(tag: String? = null, isRequired: Boolean = false, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Verbose, tag, isRequired, null, messageProvider)

inline fun Logger.d(tag: String? = null, isRequired: Boolean = false, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Debug, tag, isRequired, null, messageProvider)

inline fun Logger.i(tag: String? = null, isRequired: Boolean = false, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Info, tag, isRequired, null, messageProvider)

inline fun Logger.w(tag: String? = null, isRequired: Boolean = false, error: Throwable? = null, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Warning, tag, isRequired, error, messageProvider)

inline fun Logger.e(tag: String? = null, isRequired: Boolean = false, error: Throwable? = null, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Error, tag, isRequired, error, messageProvider)

inline fun Logger.a(tag: String? = null, isRequired: Boolean = false, error: Throwable? = null, noinline messageProvider: () -> String) =
    logCheckingUnconfinedSeverity(Level.Assert, tag, isRequired, error, messageProvider)
