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

import com.twoplaytech.drbetting.admin.data.datasource.RemoteDataSource
import com.twoplaytech.drbetting.admin.data.mappers.AccessTokenMapper
import com.twoplaytech.drbetting.admin.data.mappers.CredentialsMapper
import com.twoplaytech.drbetting.admin.data.models.*
import com.twoplaytech.drbetting.admin.util.Constants.KEY_ACCESS_TOKEN
import com.twoplaytech.drbetting.admin.util.Constants.KEY_APP_LAUNCHES
import com.twoplaytech.drbetting.admin.util.Constants.KEY_LOGGED_IN
import com.twoplaytech.drbetting.admin.util.Constants.KEY_USER_CREDENTIALS
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.datasource.LocalDataSource
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.data.models.*
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 12:18
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) :
    Repository,
    CoroutineScope {

    override fun getBettingTips(onSuccess: (List<BettingTip>) -> Unit, onError: (Message) -> Unit) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTips().catch { throwable ->
                Timber.e(throwable)
                sendErrorMessage(onError, throwable)
            }.collect { bettingTips ->
                sendBettingTips(onSuccess, bettingTips)
            }
        }
    }

    override suspend fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean
    ): Flow<Either<Message, List<BettingTip>>> =
        flow { emit(remoteDataSource.getBettingTipsBySport(sport, upcoming)) }.flowOn(
            coroutineContext
        )

    override suspend fun getBettingTipById(id: String): Flow<Either<Message, BettingTip>> = flow {
        emit(remoteDataSource.getBettingTipById(id))
    }.flowOn(coroutineContext)

    override suspend fun insertBettingTip(bettingTip: BettingTipInput): Flow<Either<Message, BettingTip>> =
        flow {
            emit(remoteDataSource.insertBettingTip(bettingTip))
        }.flowOn(coroutineContext)

    override suspend fun updateBettingTip(bettingTip: BettingTipInput): Flow<Either<Message, BettingTip>> =
        flow { emit(remoteDataSource.updateBettingTip(bettingTip)) }.flowOn(coroutineContext)

    override suspend fun deleteBettingTip(id: String): Flow<Either<Message, Int>> = flow {
        emit(remoteDataSource.deleteBettingTip(id))
    }.flowOn(coroutineContext)

    override suspend fun signIn(userInput: UserInput): Flow<Either<Message, AccessToken>> = flow {
        emit(remoteDataSource.signIn(userInput))
    }.flowOn(coroutineContext)


    override fun saveLogin(shouldStayLoggedIn: Boolean) {
        localDataSource.saveBoolean(KEY_LOGGED_IN, shouldStayLoggedIn)
    }


    override fun saveUserCredentials(email: String, password: String) {
        localDataSource.saveString(
            KEY_USER_CREDENTIALS, CredentialsMapper.toCredentialsJson(
                Credentials(email, password)
            )
        )
    }

    override fun saveToken(accessToken: AccessToken) {
        localDataSource.saveString(
            KEY_ACCESS_TOKEN,
            AccessTokenMapper.toJson(accessToken)
        )
    }

    override fun incrementAppLaunch() {
        localDataSource.getInt(KEY_APP_LAUNCHES, callback = {
            localDataSource.saveInt(KEY_APP_LAUNCHES, it.plus(1))
        })
    }

    override fun retrieveUserCredentials(
        onSuccess: (Credentials) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        localDataSource.getString(KEY_USER_CREDENTIALS, onSuccess = {
            val credentialsDataModel = CredentialsMapper.fromCredentialsJson(it)
            onSuccess.invoke(CredentialsMapper.toCredentials(credentialsDataModel))
        }, onError = {
            onError.invoke(it)
        })
    }

    override fun isAlreadyLoggedIn(callback: (Boolean) -> Unit) {
        localDataSource.getBoolean(KEY_LOGGED_IN, callback = { callback.invoke(it) })
    }

    override fun getAppLaunchesCount(callback: (Int) -> Unit) {
        localDataSource.getInt(KEY_APP_LAUNCHES, callback = {
            callback.invoke(it)
        })
    }

    override fun getAccessToken(): AccessToken?{
        return localDataSource.getString(KEY_ACCESS_TOKEN)?.let {
            AccessTokenMapper.fromJson(it)
        }
    }

    override fun sendNotification(
        notification: Notification,
        onSuccess: (Notification) -> Unit,
        onError: (Message) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.sendNotification(notification).catch { cause ->
                sendErrorMessage(onError, cause)
            }.collect {
                onSuccess.invoke(it)
            }
        }
    }

    override suspend fun sendNotification(notification: Notification): Notification {
        return remoteDataSource.sendNotification1(notification)
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private fun sendErrorMessage(
        onError: (Message) -> Unit,
        throwable: Throwable
    ) {
        if (throwable is retrofit2.HttpException) {
            val response = throwable.response()
            response?.let { serverResponse ->
                try {
                    serverResponse.errorBody()?.let { errorBody ->
                        val errorMessage = MessageMapper.fromJson(errorBody.string())
                        onError.invoke(errorMessage)
                    } ?: onError.invoke(Message("Something went wrong", 0))
                } catch (e: Exception) {
                    onError.invoke(Message("Something went wrong", 0))
                }
            } ?: onError.invoke(Message("Something went wrong", 0))
        }
    }

    override fun getToken(): AccessToken? {
        val accessTokenJson = localDataSource.getString(KEY_ACCESS_TOKEN)
        return accessTokenJson?.let {
            AccessTokenMapper.fromJson(it)
        }
    }

    private fun sendBettingTips(
        onSuccess: (List<BettingTip>) -> Unit,
        bettingTips: List<BettingTip>
    ) {
        onSuccess.invoke(bettingTips)
    }

    override fun getAppTheme(callback: (Int) -> Unit) {
        localDataSource.getInt("KEY_DARK_MODE", callback = {
            callback.invoke(it)
        })
    }

    override fun saveAppTheme(appTheme: Int) {
        localDataSource.saveInt("KEY_DARK_MODE", appTheme)
    }

    override fun userCredentials(): Credentials? {
        val credentialsJson = localDataSource.getString(KEY_USER_CREDENTIALS)
        credentialsJson?.let {
            val credentialsDataModel = CredentialsMapper.fromCredentialsJson(it)
            return CredentialsMapper.toCredentials(credentialsDataModel)
        } ?: return null
    }

    override suspend fun getTickets(): List<Ticket> = remoteDataSource.getTickets()

    override suspend fun getTicketById(id: String): Ticket = remoteDataSource.getTicketById(id)

    override suspend fun insertTicket(ticket: TicketInput): Ticket =
        remoteDataSource.insertTicket(ticket)

    override suspend fun updateTicket(ticket: TicketInput): Ticket =
        remoteDataSource.updateTicket(ticket)

    override suspend fun deleteTicketById(id: String): Message =
        remoteDataSource.deleteTicketById(id)
}