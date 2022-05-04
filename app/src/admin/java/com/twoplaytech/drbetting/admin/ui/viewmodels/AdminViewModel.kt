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

package com.twoplaytech.drbetting.admin.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.admin.data.mappers.BettingTipMapper
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.admin.domain.usecases.SendNotificationUseCase
import com.twoplaytech.drbetting.admin.ui.common.BettingTipUiState
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 31.8.21 11:28
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class AdminViewModel @Inject constructor(
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val repository: Repository
) : ViewModel() {
    private val fieldValidatorObserver = MutableLiveData<Boolean>()
    private val notificationsObserver = MutableLiveData<String>()
    private val _bettingTipUiState: MutableStateFlow<BettingTipUiState> =
        MutableStateFlow(BettingTipUiState.Neutral)
    val bettingTipUiState: StateFlow<BettingTipUiState> = _bettingTipUiState

    fun validate(validate: Boolean) {
        fieldValidatorObserver.value = validate
    }

    fun insertBettingTip(bettingTip: BettingTip) {
        viewModelScope.launch {
            repository.insertBettingTip(BettingTipMapper.toBettingTipInput(bettingTip))
                .onStart { _bettingTipUiState.value = BettingTipUiState.Loading }
                .catch { e ->
                    _bettingTipUiState.value = BettingTipUiState.Error(e)
                }.collectLatest { either ->
                    when (either) {
                        is Either.Failure -> _bettingTipUiState.value = BettingTipUiState.Error(
                            Throwable(either.message.message)
                        )
                        is Either.Response -> _bettingTipUiState.value =
                            BettingTipUiState.Success(either.data)
                    }

                }
        }
    }

    fun deleteBettingTip(id: String) {
        viewModelScope.launch {
            repository.deleteBettingTip(id)
                .onStart { _bettingTipUiState.value = BettingTipUiState.Loading }
                .catch { e ->
                    _bettingTipUiState.value = BettingTipUiState.Error(e)
                }
                .collectLatest { either ->
                    when (either) {
                        is Either.Failure -> _bettingTipUiState.value = BettingTipUiState.Error(
                            Throwable(either.message.message)
                        )
                        is Either.Response -> _bettingTipUiState.value = BettingTipUiState.Deleted
                    }
                }
        }
    }

    fun updateBettingTip(bettingTip: BettingTip) {
        viewModelScope.launch {
            repository.updateBettingTip(BettingTipMapper.toBettingTipInput(bettingTip))
                .onStart { _bettingTipUiState.value = BettingTipUiState.Loading }
                .catch { e ->
                    _bettingTipUiState.value = BettingTipUiState.Error(e)
                }
                .collectLatest { either ->
                    when (either) {
                        is Either.Failure -> _bettingTipUiState.value =
                            BettingTipUiState.Error(Throwable(either.message.message))
                        is Either.Response -> _bettingTipUiState.value =
                            BettingTipUiState.Success(either.data)
                    }
                }
        }
    }

    fun sendNotification(topic: String = "new-tips") {
        sendNotificationUseCase.sendNotification(Notification(topic), onSuccess = {
            notificationsObserver.postValue(it.message)
        }, onError = {
            notificationsObserver.postValue("Something went wrong while trying to send notification")
        })
    }

    fun observeValidation() = fieldValidatorObserver
    fun observeNotifications() = notificationsObserver
}