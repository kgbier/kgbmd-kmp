package dev.kgbier.kgbmd.util.logger

fun interface Logger {
    enum class Level {
        /**
         * Prefer [Verbose] for emitting large amounts of raw data and incidental runtime state,
         * perhaps emitted from tight loops.
         */
        Verbose,

        /**
         * Prefer [Debug] for emitting raw data which is useful for verifying behaviour.
         */
        Debug,

        /**
         * Prefer [Info] for describing decisions which have been made.
         */
        Info,

        /**
         * Prefer [Warning] when an issue might impact a feature or behaviour.
         */
        Warning,

        /**
         * Use [Error] when an issue will prevent something from working.
         */
        Error,

        /**
         * Use [Assert] when the program can do nothing other than crash.
         */
        Assert,
    }

    /**
     * Record a log
     */
    fun log(
        level: Level,
        tag: String?,
        error: Throwable?,
        messageProvider: () -> String,
    )
}


