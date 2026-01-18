package dev.kgbier.kgbmd.util.logger

class StubLogger : Logger {
    override fun log(
        level: Logger.Level,
        tag: String?,
        error: Throwable?,
        messageProvider: () -> String,
    ) = Unit
}