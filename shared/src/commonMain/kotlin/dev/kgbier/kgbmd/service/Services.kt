package dev.kgbier.kgbmd.service

import co.touchlab.kermit.Logger.Companion.d
import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.logging.*

object Services {
    val ktor: HttpClient by lazy {
        HttpClient {
            install(ContentEncoding) {
                gzip()
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        d { message }
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}