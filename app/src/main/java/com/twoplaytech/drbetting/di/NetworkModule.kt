package com.twoplaytech.drbetting.di


import com.twoplaytech.drbetting.BuildConfig
import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.admin.data.models.RefreshToken
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.admin.network.resources.users.Users
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/*
    Author: Damjan Miloshevski 
    Created on 26.4.22 16:03
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient(repository: Lazy<Repository>) = HttpClient(Android) {
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = repository.get().getAccessToken()
                    accessToken?.let {
                        return@loadTokens BearerTokens(accessToken.token, accessToken.refreshToken)
                    }
                }
                refreshTokens {
                    val userCredentials = repository.get().userCredentials()
                    oldTokens?.let { tokens ->
                        userCredentials?.let { credentials ->
                            val httpResponse = client.post(Users.RefreshToken()) {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshToken(tokens.refreshToken, credentials.email))
                            }
                            val accessToken = when (httpResponse.status) {
                                HttpStatusCode.OK -> httpResponse.body<AccessToken>()
                                else -> null
                            }
                            accessToken?.let {
                                repository.get().saveToken(accessToken)
                                return@refreshTokens BearerTokens(
                                    accessToken = accessToken.token,
                                    refreshToken = accessToken.refreshToken
                                )
                            }
                        }
                    }
                }
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = TimeUnit.MINUTES.toMillis(1L)
            connectTimeoutMillis = TimeUnit.MINUTES.toMillis(1L)
            socketTimeoutMillis = TimeUnit.MINUTES.toMillis(2L)
        }
        install(DefaultRequest) {
            url(BuildConfig.BASE_URL)
        }
        install(Resources)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }
        engine {
            // this: AndroidEngineConfig
            connectTimeout = TimeUnit.SECONDS.toMillis(30L).toInt()
            socketTimeout = TimeUnit.SECONDS.toMillis(30L).toInt()
        }
    }
}