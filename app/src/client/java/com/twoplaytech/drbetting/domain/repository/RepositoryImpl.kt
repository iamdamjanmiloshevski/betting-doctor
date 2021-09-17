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
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.ui.util.Constants.KEY_APP_LAUNCHES
import com.twoplaytech.drbetting.ui.util.Constants.KEY_NEW_TIPS_NOTIFICATIONS
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

    override fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean,
        onSuccess: (List<BettingTip>) -> Unit,
        onError: (Message) -> Unit
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
        onError: (Message) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTipById(id).catch { throwable ->
                sendErrorMessage(onError, throwable)
            }.collect { bettingTip ->
                onSuccess.invoke(bettingTip)
            }
        }
    }


    override fun incrementAppLaunch() {
        localDataSource.getInt(KEY_APP_LAUNCHES, callback = {
            localDataSource.saveInt(KEY_APP_LAUNCHES, it.plus(1))
        })
    }


    override fun getAppLaunchesCount(callback: (Int) -> Unit) {
        localDataSource.getInt(KEY_APP_LAUNCHES, callback = {
            callback.invoke(it)
        })
    }

    override fun receiveNotifications(topic: String) {
        when (topic) {
            "new-tips" -> {
                localDataSource.saveBoolean(KEY_NEW_TIPS_NOTIFICATIONS, true)
            }
        }
    }

    override fun sendFeedback(
        feedbackMessage: FeedbackMessage,
        onSuccess: (FeedbackMessage) -> Unit,
        onError: (Message) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.sendFeedback(feedbackMessage).catch { cause ->
                sendErrorMessage(onError, cause)
            }.collect { feedbackMessage ->
                onSuccess.invoke(feedbackMessage)
            }
        }
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

    private fun sendBettingTips(
        onSuccess: (List<BettingTip>) -> Unit,
        bettingTips: List<BettingTip>
    ) {
        onSuccess.invoke(bettingTips)
    }
}