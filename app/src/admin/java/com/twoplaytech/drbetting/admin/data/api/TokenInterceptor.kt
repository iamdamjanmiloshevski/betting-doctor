package com.twoplaytech.drbetting.admin.data.api

import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.admin.util.hasExpiredToken
import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 8.11.21 12:56
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class TokenInterceptor @Inject constructor(private val repository: Lazy<Repository>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = repository.get().getToken()
        val token = accessToken?.token
        val authenticatedRequest = request(request, token)
        val initialResponse = chain.proceed(authenticatedRequest)
        val newAuthRequest = accessToken?.let {
            when (initialResponse.code) {
                403, 401 -> {
                    val hasExpired = accessToken.hasExpiredToken()
                    if (!hasExpired) {
                        Timber.i("Token hasn't expired")
                        request(request,token)
                    } else {
                        Timber.i("Token has expired!")
                        val newToken = repository.get().getRefreshTokenAsync()
                        request(request,newToken.token)
                    }
                }
                else -> {
                    authenticatedRequest
                }
            }
        }
        return chain.proceed(newAuthRequest ?: authenticatedRequest)
    }

    private fun request(originalRequest: Request, token: String?): Request {
        return originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token").build()
    }
}