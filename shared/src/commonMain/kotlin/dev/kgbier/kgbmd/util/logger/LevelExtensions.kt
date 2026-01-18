package dev.kgbier.kgbmd.util.logger

import co.touchlab.kermit.Severity

fun Logger.Level.toKermit(): Severity = when (this) {
    Logger.Level.Verbose -> Severity.Verbose
    Logger.Level.Debug -> Severity.Debug
    Logger.Level.Info -> Severity.Info
    Logger.Level.Warning -> Severity.Warn
    Logger.Level.Error -> Severity.Error
    Logger.Level.Assert -> Severity.Assert
}
