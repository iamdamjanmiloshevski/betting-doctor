package com.twoplaytech.drbetting.sportsanalyst.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.twoplaytech.drbetting.BuildConfig
import com.twoplaytech.drbetting.sportsanalyst.data.api.TicketsApi
import com.twoplaytech.drbetting.util.GsonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 12:07
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        client.addInterceptor(StethoInterceptor())
        client.addInterceptor(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            ) else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        )
        return client.build()
    }

    @Singleton

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonUtil.gson))
        .build()

    @Provides
    fun provideTicketsApi(retrofit: Retrofit): TicketsApi = retrofit.create(TicketsApi::class.java)
}