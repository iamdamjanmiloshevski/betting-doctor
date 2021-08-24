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

package com.twoplaytech.drbetting.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.data.entities.BettingTip
import com.twoplaytech.drbetting.data.entities.Message
import com.twoplaytech.drbetting.data.entities.Sport
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.usecases.DeleteBettingTipUseCase
import com.twoplaytech.drbetting.domain.usecases.GetBettingTipsUseCase
import com.twoplaytech.drbetting.domain.usecases.InsertBettingTipUseCase
import com.twoplaytech.drbetting.domain.usecases.UpdateBettingTipUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val getBettingTipsUseCase: GetBettingTipsUseCase,
    private val insertBettingTipUseCase: InsertBettingTipUseCase,
    private val updateBettingTipUseCase: UpdateBettingTipUseCase,
    private val deleteBettingTipUseCase: DeleteBettingTipUseCase
) :
    ViewModel() {
    private val fieldValidatorObserver = MutableLiveData<Boolean>()
    private val insertBettingTipObserver = MutableLiveData<Resource<BettingTip>>()
    private val deleteBettingTipObserver = MutableLiveData<Resource<Message>>()
    private val updateBettingTipObserver = MutableLiveData<Resource<BettingTip>>()
    private val bettingTipsObserver = MutableLiveData<Resource<Any>>()


    fun validate(validate: Boolean) {
        fieldValidatorObserver.value = validate
    }

    fun getBettingTips(sport: Sport, upcoming: Boolean) {
        getBettingTipsUseCase.getBettingTipsBySport(sport, upcoming, onSuccess = { bettingTips ->
            bettingTipsObserver.value = Resource.success(null, bettingTips)
        }, onError = { throwable ->
            bettingTipsObserver.value = Resource.error(null, throwable.localizedMessage)
        })
    }

    fun insertBettingTip(bettingTip: BettingTip) {
        insertBettingTipUseCase.insertBettingTip(bettingTip, onSuccess = { updatedBettingTip ->
            insertBettingTipObserver.value = Resource.success(null, updatedBettingTip)
        }, onError = { cause ->
            insertBettingTipObserver.value = Resource.error(cause.localizedMessage, null)
        })
    }

    fun deleteBettingTip(id:String){
        deleteBettingTipUseCase.deleteBettingTip(id,onSuccess = {message ->
            deleteBettingTipObserver.value = Resource.success(message.message,message)
        },onError = {cause->
            deleteBettingTipObserver.value = Resource.error(cause.message,null)
        })
    }

    fun updateBettingTip(bettingTip: BettingTip){
        updateBettingTipUseCase.updateBettingTip(bettingTip._id,bettingTip,onSuccess = { bettingTip ->
            updateBettingTipObserver.value = Resource.success(null,bettingTip)
        },onError = { cause->
            updateBettingTipObserver.value = Resource.error(cause.message,null)
        })
    }

    fun observeValidation() = fieldValidatorObserver

    fun observeForInsertedBettingTip() = insertBettingTipObserver

    fun observeDeletedTip() = deleteBettingTipObserver

    fun observeOnUpdatedTip()= updateBettingTipObserver

    fun observeTips() = bettingTipsObserver
}