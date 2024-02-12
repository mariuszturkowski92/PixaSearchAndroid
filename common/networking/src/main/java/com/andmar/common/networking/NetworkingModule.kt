package com.andmar.common.networking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        return createHttpClient(enableLogging = true)
    }

    fun createHttpClient(enableLogging: Boolean): HttpClient {
        return HttpClient(OkHttp).config {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
                this.logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
            }
        }
    }

}