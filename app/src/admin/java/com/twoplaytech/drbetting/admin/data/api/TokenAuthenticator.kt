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

package com.twoplaytech.drbetting.admin.data.api


import com.twoplaytech.drbetting.admin.domain.repository.Repository
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 11:55
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class TokenAuthenticator @Inject constructor(private val repository: Lazy<Repository>) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return if (response.code == 401) {
            runBlocking {
                val refreshToken = repository.get().getRefreshTokenAsync()
                repository.get().saveToken(refreshToken)
                response.request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ".plus(refreshToken.token)).build()
            }
        } else {
            runBlocking {
                val accessToken = repository.get().getAccessTokenAsync()
                response.request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ".plus(accessToken.token)).build()
            }

        }
    }
}