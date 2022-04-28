package com.twoplaytech.drbetting.di


import com.twoplaytech.drbetting.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.serialization.gson.*
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
    fun provideHttpClient() = HttpClient(Android) {
        install(Logging){
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(HttpTimeout){
            requestTimeoutMillis = TimeUnit.MINUTES.toMillis(1L)
            connectTimeoutMillis = TimeUnit.MINUTES.toMillis(1L)
            socketTimeoutMillis = TimeUnit.MINUTES.toMillis(2L)
        }
        install(DefaultRequest){
            url(BuildConfig.BASE_URL)
        }
        install(Resources)
        install(ContentNegotiation){
            gson {
                setPrettyPrinting()
                disableHtmlEscaping()
            }
        }
        engine {
            // this: AndroidEngineConfig
            connectTimeout = TimeUnit.SECONDS.toMillis(30L).toInt()
            socketTimeout =  TimeUnit.SECONDS.toMillis(30L).toInt()
        }
    }
}