package dev.kgbier.kgbmd.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import co.touchlab.kermit.Logger as KLogger

class KtorModule {
    val ktor: HttpClient by lazy {
        HttpClient {
            install(ContentEncoding) {
                gzip()
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        KLogger.d { message }
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}
