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

package com.twoplaytech.drbetting.data.datasource

import com.twoplaytech.drbetting.data.api.BettingDoctorAPI
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.network.resources.bettingtips.BettingTips
import com.twoplaytech.drbetting.network.resources.users.Users
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 15:25
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient,
    private val api: BettingDoctorAPI
) :
    RemoteDataSource, CoroutineScope {
    override suspend fun getBettingTips(): Flow<List<BettingTip>> {
        return flow { emit(api.getBettingTips()) }.flowOn(coroutineContext)
    }

    override suspend fun getBettingTipsKtor(): Either<Message, List<BettingTip>> {
        return try {
            val httpResponse = client.get(BettingTips)
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
                HttpStatusCode.NoContent -> Either.Failure(httpResponse.body())
                else -> Either.Failure(httpResponse.body())
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ServerResponseException) {
            // 5xx - response
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        }
    }

    override suspend fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean
    ): Flow<List<BettingTip>> {
        return when (upcoming) {
            true -> flow { emit(api.getUpcomingBettingTipsBySport(sport)) }.flowOn(coroutineContext)
            false -> flow { emit(api.getOlderBettingTipsBySport(sport)) }.flowOn(coroutineContext)
        }
    }

    override suspend fun getBettingTipsBySportKtor(
        sport: Sport,
        upcoming: Boolean
    ): Either<Message, List<BettingTip>> {
        when (upcoming) {
            true -> {
                return try {
                    val httpResponse =
                        client.get(BettingTips.Sport.Upcoming(BettingTips.Sport(sport = sport.name)))
                    when (httpResponse.status) {
                        HttpStatusCode.OK -> Either.Response(httpResponse.body())
                        HttpStatusCode.NoContent -> Either.Failure(httpResponse.body())
                        else -> Either.Failure(httpResponse.body())
                    }
                } catch (ex: RedirectResponseException) {
                    // 3xx - responses
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                } catch (ex: ClientRequestException) {
                    // 4xx - responses
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                } catch (ex: ServerResponseException) {
                    // 5xx - response
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                }
            }
            false -> {
                return try {
                    val httpResponse =
                        client.get(BettingTips.Sport.Older(BettingTips.Sport(sport = sport.name)))
                    when (httpResponse.status) {
                        HttpStatusCode.OK -> Either.Response(httpResponse.body())
                        HttpStatusCode.NoContent -> Either.Failure(httpResponse.body())
                        else -> Either.Failure(httpResponse.body())
                    }
                } catch (ex: RedirectResponseException) {
                    // 3xx - responses
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                } catch (ex: ClientRequestException) {
                    // 4xx - responses
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                } catch (ex: ServerResponseException) {
                    // 5xx - response
                    Timber.e(ex)
                    Either.Failure(Message(message = ex.cause?.message ?: "Error"))
                }
            }
        }
    }


    override suspend fun getBettingTipById(id: String): Flow<BettingTip> {
        return flow { emit(api.getBettingTipById(id)) }.flowOn(coroutineContext)
    }

    override suspend fun getBettingTipByIdKtor(id: String): Either<Message, BettingTip> {
        return try {
            val httpResponse = client.get(BettingTips.Id(id = id))
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
                HttpStatusCode.NoContent -> Either.Failure(httpResponse.body())
                else -> Either.Failure(httpResponse.body())
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ServerResponseException) {
            // 5xx - response
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        }
    }

    override suspend fun sendFeedback(feedbackMessage: FeedbackMessage): Flow<FeedbackMessage> {
        return flow { emit(api.sendFeedback(feedbackMessage)) }.flowOn(coroutineContext)
    }

    override suspend fun sendFeedbackKtor(feedbackMessage: FeedbackMessage): Either<Message, FeedbackMessage> {
        return try {
            val httpResponse = client.post(Users.Feedback()) {
                contentType(ContentType.Application.Json)
                setBody(feedbackMessage)
            }
            when (httpResponse.status) {
                HttpStatusCode.Created -> Either.Response(httpResponse.body())
                HttpStatusCode.NoContent -> Either.Failure(httpResponse.body())
                else -> {
                    val message = httpResponse.body() as Message
                    Timber.e("$message")
                    Either.Failure(message)
                }
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        } catch (ex: ServerResponseException) {
            // 5xx - response
            Timber.e(ex)
            Either.Failure(Message(message = ex.cause?.message ?: "Error"))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}