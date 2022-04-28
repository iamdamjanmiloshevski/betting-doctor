package com.twoplaytech.drbetting.sportsanalyst.data.datasource

import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.sportsanalyst.network.resources.Tickets
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 12:04
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : RemoteDataSource,
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override suspend fun getTicketByDate(date: String): Either<Message, Ticket> {
        return try {
            val httpResponse = client.get(Tickets(date))
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