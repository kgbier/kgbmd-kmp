package dev.kgbier.kgbmd.util.logger

class DefaultTaggedLogger(
    private val delegate: Logger,
    private val defaultTag: String,
) : Logger {

    override fun log(
        level: Logger.Level,
        tag: String?,
        error: Throwable?,
        messageProvider: () -> String,
    ) = delegate.log(
        level = level,
        tag = tag ?: defaultTag,
        error = error,
        messageProvider = messageProvider,
    )
}

/**
 * Decorate a logger with an implicit tag
 */
fun Logger.tagged(tag: String): Logger = DefaultTaggedLogger(
    delegate = this,
    defaultTag = tag,
)
