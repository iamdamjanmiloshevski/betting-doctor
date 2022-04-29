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

import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.datasource.LocalDataSource
import com.twoplaytech.drbetting.data.datasource.RemoteDataSource
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.data.models.*
import com.twoplaytech.drbetting.ui.util.Constants.KEY_APP_LAUNCHES
import com.twoplaytech.drbetting.ui.util.Constants.KEY_DARK_MODE
import com.twoplaytech.drbetting.ui.util.Constants.KEY_NEW_TIPS_NOTIFICATIONS
import com.twoplaytech.drbetting.ui.util.Constants.KEY_RATE_US
import com.twoplaytech.drbetting.ui.util.Constants.KEY_RATING_COMPLETED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
                sendErrorMessage(onError, throwable)
            }.collect { bettingTips ->
                sendBettingTips(onSuccess, bettingTips)
            }
        }
    }

    override suspend fun getBettingTips(): Flow<Either<Message, List<BettingTip>>> =
        flow { emit(remoteDataSource.getBettingTipsKtor()) }.flowOn(coroutineContext)


    override fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean,
        onSuccess: (List<BettingTip>) -> Unit,
        onError: (Message) -> Unit
    ) {
        launch(coroutineContext) {
            remoteDataSource.getBettingTipsBySport(sport, upcoming).catch { throwable ->
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
        flow { emit(remoteDataSource.getBettingTipsBySportKtor(sport, upcoming)) }

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

    override suspend fun getBettingTipById(id: String): Flow<Either<Message, BettingTip>> = flow {
        emit(remoteDataSource.getBettingTipByIdKtor(id))
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

    override suspend fun sendFeedback(feedbackMessage: FeedbackMessage): Flow<Either<Message, FeedbackMessage>> =
        flow {
            emit(remoteDataSource.sendFeedbackKtor(feedbackMessage))
        }.flowOn(coroutineContext)

    override fun getAppTheme(callback: (Int) -> Unit) {
        localDataSource.getInt(KEY_DARK_MODE, callback = {
            callback.invoke(it)
        })
    }

    override fun saveAppTheme(appTheme: Int) {
        localDataSource.saveInt(KEY_DARK_MODE, appTheme)
    }

    override fun getRateUs(callback: (RateUs, Boolean) -> Unit) {
        localDataSource.getBoolean(KEY_RATING_COMPLETED) { isCompleted ->
            localDataSource.getInt(KEY_RATE_US, callback = { rateUs ->
                when (rateUs) {
                    0 -> {
                        callback.invoke(RateUs.Rate_Us, isCompleted)
                    }
                    1 -> {
                        callback.invoke(RateUs.No, isCompleted)
                    }
                    2 -> {
                        callback.invoke(RateUs.MaybeLater, isCompleted)
                    }
                    else -> {
                        callback.invoke(RateUs.MaybeLater, isCompleted)
                    }
                }
            })
        }
    }

    override fun saveRateUs(rateUs: RateUs, isCompleted: Boolean) {
        localDataSource.saveInt(KEY_RATE_US, rateUs.value)
        localDataSource.saveBoolean(KEY_RATING_COMPLETED, isCompleted)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private fun sendErrorMessage(
        onError: (Message) -> Unit,
        throwable: Throwable
    ) {
        Timber.e(throwable)
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