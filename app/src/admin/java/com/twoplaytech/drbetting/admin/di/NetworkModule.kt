/*
 * MIT License
 *
 * Copyright (c)  2021 Damjan Miloshevski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.twoplaytech.drbetting.admin.di

import com.twoplaytech.drbetting.BuildConfig
import com.twoplaytech.drbetting.admin.data.api.BettingDoctorAPI
import com.twoplaytech.drbetting.admin.data.api.TokenAuthenticator
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import dagger.Lazy
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
    Created on 9.9.21 14:40
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(repository: Lazy<Repository>): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        client.authenticator(TokenAuthenticator(repository))
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
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    fun provideApi(retrofit: Retrofit): BettingDoctorAPI =
        retrofit.create(BettingDoctorAPI::class.java)
}