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
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.data.models.UserInput
import com.twoplaytech.drbetting.data.models.*
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 15:23
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface RemoteDataSource {
    suspend fun getBettingTips(): Flow<List<BettingTip>>
    suspend fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean = false
    ): Flow<List<BettingTip>>

    suspend fun getBettingTipById(
        id: String
    ): Flow<BettingTip>

    suspend fun insertBettingTip(bettingTip: BettingTipInput): Flow<BettingTip>

    suspend fun updateBettingTip(bettingTip: BettingTipInput): Flow<BettingTip>

    suspend fun deleteBettingTip(id: String): Flow<Message>

    suspend fun signIn(userInput: UserInput): Flow<AccessToken>
    suspend fun refreshToken(refreshToken: String): Flow<AccessToken>
    suspend fun refreshTokenAsync(refreshToken: String): AccessToken
    suspend fun sendNotification(topic:String):Flow<Unit>


    suspend fun getTickets():List<Ticket>
    suspend fun getTicketById( id:String): Ticket
    suspend fun insertTicket(ticket: TicketInput): Ticket
    suspend fun updateTicket( ticket: TicketInput): Ticket
    suspend fun deleteTicketById(id:String):Message
}