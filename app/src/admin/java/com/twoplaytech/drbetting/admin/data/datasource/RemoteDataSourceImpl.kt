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

import com.twoplaytech.drbetting.admin.data.api.BettingDoctorAPI
import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.admin.data.models.RefreshToken
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.data.models.UserInput
import com.twoplaytech.drbetting.data.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 15:25
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RemoteDataSourceImpl @Inject constructor(private val api: BettingDoctorAPI) :
    RemoteDataSource, CoroutineScope {
    override suspend fun getBettingTips(): Flow<List<BettingTip>> {
        return flow { emit(api.getBettingTips()) }.flowOn(coroutineContext)
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

    override suspend fun getBettingTipById(id: String): Flow<BettingTip> {
        return flow { emit(api.getBettingTipById(id)) }.flowOn(coroutineContext)
    }

    override suspend fun insertBettingTip(bettingTip: BettingTipInput): Flow<BettingTip> {
        return flow { emit(api.insertBettingTip(bettingTip)) }.flowOn(coroutineContext)
    }

    override suspend fun updateBettingTip(bettingTip: BettingTipInput): Flow<BettingTip> {
        return flow { emit(api.updateBettingTip(bettingTip)) }.flowOn(coroutineContext)
    }

    override suspend fun deleteBettingTip(id: String): Flow<Message> {
        return flow { emit(api.deleteBettingTip(id)) }.flowOn(coroutineContext)
    }

    override suspend fun signIn(userInput: UserInput): Flow<AccessToken> {
        return flow { emit(api.signIn(userInput)) }.flowOn(coroutineContext)
    }


    override suspend fun refreshToken(refreshToken: RefreshToken): AccessToken =
        api.refreshToken(refreshToken)



    override suspend fun sendNotification(topic: String): Flow<Unit> {
        return flow { emit(api.sendNotification(topic)) }.flowOn(coroutineContext)

    }

    override suspend fun getTickets(): List<Ticket> {
        return api.getTickets()
    }

    override suspend fun getTicketById(id: String): Ticket {
        return api.getTicketById(id)
    }

    override suspend fun insertTicket(ticket: TicketInput): Ticket {
        return api.insertTicket(ticket)
    }

    override suspend fun updateTicket(ticket: TicketInput): Ticket {
        return api.updateTicket(ticket)
    }

    override suspend fun deleteTicketById(id: String): Message {
        return api.deleteTicketById(id)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}