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

package com.twoplaytech.drbetting.admin.data.datasource

import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.admin.data.models.RefreshToken
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.data.models.UserInput
import com.twoplaytech.drbetting.admin.network.resources.tickets.Tickets
import com.twoplaytech.drbetting.admin.network.resources.users.Users
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.*
import com.twoplaytech.drbetting.network.resources.bettingtips.BettingTips
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 15:25
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RemoteDataSourceImpl @Inject constructor(private val client:HttpClient) :
    RemoteDataSource, CoroutineScope {

    override suspend fun getBettingTipsBySport(
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

    override suspend fun getBettingTipById(id: String): Either<Message, BettingTip> {
        return try {
            val httpResponse =
                client.get(BettingTips.Id(id = id))
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun insertBettingTip(bettingTip: BettingTipInput): Either<Message, BettingTip> {
        return try {
            val httpResponse = client.post(BettingTips()){
                contentType(ContentType.Application.Json)
                setBody(bettingTip)
            }
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun updateBettingTip(bettingTip: BettingTipInput): Either<Message, BettingTip> {
        return try {
            val httpResponse = client.put(BettingTips()){
                contentType(ContentType.Application.Json)
                setBody(bettingTip)
            }
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun deleteBettingTip(id: String): Either<Message, Int> {
        return try {
            val httpResponse = client.delete(BettingTips.Id(id = id))
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun signIn(userInput: UserInput): Either<Message, AccessToken> {
        return try {
            val httpResponse = client.post(Users.Login()){
                contentType(ContentType.Application.Json)
                setBody(userInput)
            }
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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


    override suspend fun refreshToken(refreshToken: RefreshToken): Either<Message, AccessToken> {
        return try {
            val httpResponse = client.post(Users.RefreshToken()){
                contentType(ContentType.Application.Json)
                setBody(refreshToken)
            }
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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


    override suspend fun sendNotification(notification: Notification): Either<Message, Notification> {
        return try {
            val httpResponse = client.post(Users.Notification()){
                contentType(ContentType.Application.Json)
                setBody(notification)
            }
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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



    override suspend fun getTickets(): Either<Message, List<Ticket>> {
        return try {
            val httpResponse = client.get(Tickets())
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun getTicketById(id: String): Either<Message, Ticket> {
        return try {
            val httpResponse = client.get(Tickets.Id(id = id))
            when (httpResponse.status) {
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
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

    override suspend fun insertTicket(ticket: TicketInput): Either<Message, Ticket> {
        return try{
            val httpResponse = client.post(Tickets()){
                contentType(ContentType.Application.Json)
                setBody(ticket)
            }
            when(httpResponse.status){
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
                else -> Either.Failure(httpResponse.body())
            }
        }catch (ex: RedirectResponseException) {
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

    override suspend fun updateTicket(ticket: TicketInput): Either<Message, Ticket> {
        return try{
            val httpResponse = client.put(Tickets()){
                contentType(ContentType.Application.Json)
                setBody(ticket)
            }
            when(httpResponse.status){
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
                else -> Either.Failure(httpResponse.body())
            }
        }catch (ex: RedirectResponseException) {
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

    override suspend fun deleteTicketById(id: String): Either<Message, Message> {
        return try{
            val httpResponse = client.delete(Tickets.Id(id = id))
            when(httpResponse.status){
                HttpStatusCode.OK -> Either.Response(httpResponse.body())
                else -> Either.Failure(httpResponse.body())
            }
        }catch (ex: RedirectResponseException) {
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