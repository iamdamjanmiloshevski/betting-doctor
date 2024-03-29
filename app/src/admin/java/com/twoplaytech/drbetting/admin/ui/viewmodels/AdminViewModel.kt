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
import com.twoplaytech.drbetting.admin.data.mappers.BettingTipMapper
import com.twoplaytech.drbetting.admin.domain.usecases.DeleteBettingTipUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.InsertBettingTipUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.SendNotificationUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.UpdateBettingTipUseCase
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Notification
import com.twoplaytech.drbetting.domain.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 31.8.21 11:28
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class AdminViewModel @Inject constructor(
    private val insertBettingTipUseCase: InsertBettingTipUseCase,
    private val updateBettingTipUseCase: UpdateBettingTipUseCase,
    private val deleteBettingTipUseCase: DeleteBettingTipUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {
    private val insertBettingTipObserver = MutableLiveData<Resource<BettingTip>>()
    private val deleteBettingTipObserver = MutableLiveData<Resource<Message>>()
    private val updateBettingTipObserver = MutableLiveData<Resource<BettingTip>>()
    private val fieldValidatorObserver = MutableLiveData<Boolean>()
    private val notificationsObserver = MutableLiveData<String>()
    fun validate(validate: Boolean) {
        fieldValidatorObserver.value = validate
    }

    fun insertBettingTip(bettingTip: BettingTip) {
        insertBettingTipObserver.postValue(Resource.loading(null, null))
        insertBettingTipUseCase.insertBettingTip(BettingTipMapper.toBettingTipInput(bettingTip), onSuccess = { updatedBettingTip ->
            insertBettingTipObserver.postValue(Resource.success(null, updatedBettingTip))
        }, onError = { message ->
            insertBettingTipObserver.postValue(Resource.error(message.message, null))
        })
    }

    fun deleteBettingTip(id: String) {
        deleteBettingTipUseCase.deleteBettingTip(id, onSuccess = { message ->
            deleteBettingTipObserver.postValue(Resource.success(message.message, message))
        }, onError = { message ->
            deleteBettingTipObserver.postValue(Resource.error(message.message, null))
        })
    }

    fun updateBettingTip(bettingTip: BettingTip) {
        updateBettingTipObserver.postValue(Resource.loading(null, null))
        updateBettingTipUseCase.updateBettingTip(BettingTipMapper.toBettingTipInput(bettingTip), onSuccess = {
            updateBettingTipObserver.postValue(Resource.success(null, it))
        }, onError = { message ->
            updateBettingTipObserver.postValue(Resource.error(message.message, null))
        })
    }

    fun sendNotification(topic: String = "new-tips") {
        sendNotificationUseCase.sendNotification(Notification(topic), onSuccess = {
            notificationsObserver.postValue(it.message)
        }, onError = {
            notificationsObserver.postValue("Something went wrong while trying to send notification")
        })
    }

    fun observeValidation() = fieldValidatorObserver

    fun observeForInsertedBettingTip() = insertBettingTipObserver

    fun observeDeletedTip() = deleteBettingTipObserver

    fun observeOnUpdatedTip() = updateBettingTipObserver

    fun observeNotifications() = notificationsObserver
}