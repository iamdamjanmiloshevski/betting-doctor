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
import com.twoplaytech.drbetting.data.entities.BettingTip
import com.twoplaytech.drbetting.data.entities.Message
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.usecases.DeleteBettingTipUseCase
import com.twoplaytech.drbetting.domain.usecases.InsertBettingTipUseCase
import com.twoplaytech.drbetting.domain.usecases.UpdateBettingTipUseCase
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
    private val deleteBettingTipUseCase: DeleteBettingTipUseCase
) : ViewModel() {
    private val insertBettingTipObserver = MutableLiveData<Resource<BettingTip>>()
    private val deleteBettingTipObserver = MutableLiveData<Resource<Message>>()
    private val updateBettingTipObserver = MutableLiveData<Resource<BettingTip>>()

    fun insertBettingTip(bettingTip: BettingTip) {
        insertBettingTipUseCase.insertBettingTip(bettingTip, onSuccess = { updatedBettingTip ->
            insertBettingTipObserver.postValue(Resource.success(null, updatedBettingTip))
        }, onError = { cause ->
            insertBettingTipObserver.postValue(Resource.error(cause.localizedMessage, null))
        })
    }

    fun deleteBettingTip(id:String){
        deleteBettingTipUseCase.deleteBettingTip(id,onSuccess = {message ->
            deleteBettingTipObserver.postValue( Resource.success(message.message,message))
        },onError = {cause->
            deleteBettingTipObserver.postValue(Resource.error(cause.message,null))
        })
    }

    fun updateBettingTip(bettingTip: BettingTip){
        updateBettingTipUseCase.updateBettingTip(bettingTip._id,bettingTip,onSuccess = { bettingTip ->
            updateBettingTipObserver.postValue(Resource.success(null,bettingTip))
        },onError = { cause->
            updateBettingTipObserver.postValue(Resource.error(cause.message,null))
        })
    }
    fun observeForInsertedBettingTip() = insertBettingTipObserver

    fun observeDeletedTip() = deleteBettingTipObserver

    fun observeOnUpdatedTip()= updateBettingTipObserver
}