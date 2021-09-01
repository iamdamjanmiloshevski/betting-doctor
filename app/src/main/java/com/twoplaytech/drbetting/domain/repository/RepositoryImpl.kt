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

package com.twoplaytech.drbetting.domain.repository

import com.twoplaytech.drbetting.data.datasource.LocalDataSource
import com.twoplaytech.drbetting.data.datasource.RemoteDataSource
import com.twoplaytech.drbetting.data.entities.*
import com.twoplaytech.drbetting.data.mappers.AccessTokenMapper
import com.twoplaytech.drbetting.data.mappers.CredentialsMapper
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_ACCESS_TOKEN
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_LOGGED_IN
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_USER_CREDENTIALS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    override fun getBettingTips(
        onSuccess: (List<BettingTip>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTips().catch { throwable ->
                Timber.e(throwable)
                sendErrorMessage(onError, throwable)
            }.collect { bettingTips ->
                sendBettingTips(onSuccess, bettingTips)
            }
        }
    }

    override fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean,
        onSuccess: (List<BettingTip>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTipsBySport(sport, upcoming).catch { throwable ->
                sendErrorMessage(onError, throwable)
            }.collect { bettingTips ->
                sendBettingTips(onSuccess, bettingTips)
            }
        }
    }

    override fun getBettingTipById(
        id: String,
        onSuccess: (BettingTip) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTipById(id).catch { throwable ->
                sendErrorMessage(onError, throwable)
            }.collect { bettingTip ->
                onSuccess.invoke(bettingTip)
            }
        }
    }

    override fun insertBettingTip(
        bettingTip: BettingTip,
        onSuccess: (BettingTip) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.insertBettingTip(bettingTip).catch { throwable ->
                sendErrorMessage(onError, throwable)
            }.collect { bettingTip -> onSuccess.invoke(bettingTip) }
        }
    }

    override fun updateBettingTip(
        id: String,
        bettingTip: BettingTip,
        onSuccess: (BettingTip) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.updateBettingTip(id, bettingTip).catch { cause ->
                sendErrorMessage(onError, cause)
            }.collect { bettingTip: BettingTip ->
                onSuccess.invoke(bettingTip)
            }
        }
    }

    override fun deleteBettingTip(
        id: String,
        onSuccess: (Message) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.deleteBettingTip(id)
                .catch { cause -> sendErrorMessage(onError, cause) }
                .collect { message: Message -> onSuccess.invoke(message) }
        }
    }

    override fun signIn(
        userInput: UserInput,
        onSuccess: (AccessToken) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.signIn(userInput).catch { throwable ->
                sendErrorMessage(onError, throwable)
            }.collect { accessToken ->
                localDataSource.saveString(
                    KEY_ACCESS_TOKEN,
                    AccessTokenMapper.toJson(accessToken)
                )
                onSuccess.invoke(accessToken)
            }
        }
    }

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

    override fun retrieveUserCredentials(
        onSuccess: (Credentials) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        localDataSource.getString(KEY_USER_CREDENTIALS,onSuccess = {
            val credentialsDataModel = CredentialsMapper.fromCredentialsJson(it)
            onSuccess.invoke(CredentialsMapper.toCredentials(credentialsDataModel))
        },onError ={
            onError.invoke(it)
        })
    }

    override fun isAlreadyLoggedIn(callback: (Boolean) -> Unit) {
        localDataSource.getBoolean(KEY_LOGGED_IN,callback = { callback.invoke(it) })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private fun sendErrorMessage(
        onError: (Throwable) -> Unit,
        throwable: Throwable
    ) {
        onError.invoke(throwable)
    }

    private fun sendBettingTips(
        onSuccess: (List<BettingTip>) -> Unit,
        bettingTips: List<BettingTip>
    ) {
        onSuccess.invoke(bettingTips)
    }
}