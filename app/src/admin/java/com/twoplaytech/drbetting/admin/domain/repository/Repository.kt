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

package com.twoplaytech.drbetting.admin.domain.repository

import com.twoplaytech.drbetting.admin.data.models.*
import com.twoplaytech.drbetting.data.models.*

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 12:18
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface Repository {
    fun getBettingTips(onSuccess: (List<BettingTip>) -> Unit, onError: (Message) -> Unit)
    fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean = false,
        onSuccess: (List<BettingTip>) -> Unit,
        onError: (Message) -> Unit
    )

    fun getBettingTipById(id: String, onSuccess: (BettingTip) -> Unit, onError: (Message) -> Unit)
    fun insertBettingTip(
        bettingTip: BettingTipInput,
        onSuccess: (BettingTip) -> Unit,
        onError: (Message) -> Unit
    )

    fun updateBettingTip(
        bettingTip: BettingTipInput,
        onSuccess: (BettingTip) -> Unit,
        onError: (Message) -> Unit
    )

    fun deleteBettingTip(id: String, onSuccess: (Message) -> Unit, onError: (Message) -> Unit)

    fun signIn(
        userInput: UserInput,
        onSuccess: (AccessToken) -> Unit,
        onError: (Message) -> Unit
    )

    fun saveLogin(shouldStayLoggedIn: Boolean)
    fun saveUserCredentials(email: String, password: String)
    fun saveToken(accessToken: AccessToken)

    fun incrementAppLaunch()

    fun retrieveUserCredentials(onSuccess: (Credentials) -> Unit, onError: (Throwable) -> Unit)
    fun isAlreadyLoggedIn(callback: (Boolean) -> Unit)

    fun getAppLaunchesCount(callback: (Int) -> Unit)


    fun getAccessToken(
        onSuccess: (AccessToken) -> Unit,
        onError: (Message) -> Unit
    )
    fun refreshToken(refreshToken: RefreshToken): AccessToken
    fun getToken(): AccessToken?
    suspend fun getAccessTokenAsync(): AccessToken


    fun sendNotification(
        notification: Notification,
        onSuccess: (Notification) -> Unit,
        onError: (Message) -> Unit
    )
    suspend fun sendNotification(notification: Notification):Notification


    fun getAppTheme(callback: (Int) -> Unit)
    fun saveAppTheme(appTheme: Int)

    fun userCredentials(): Credentials?

    suspend fun getTickets(): List<Ticket>
    suspend fun getTicketById(id: String): Ticket
    suspend fun insertTicket(ticket: TicketInput): Ticket
    suspend fun updateTicket(ticket: TicketInput): Ticket
    suspend fun deleteTicketById(id: String): Message


}