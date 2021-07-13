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
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.BettingTip
import com.twoplaytech.drbetting.data.Resource
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.repository.BettingTipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val bettingTipsRepository: BettingTipsRepository
) :
    ViewModel() {
    private val olderTipsObserver = MutableLiveData<Resource<List<BettingTip>>>()
    private val fieldValidatorObserver = MutableLiveData<Boolean>()
    private val saveObserver = MutableLiveData<Resource<Boolean>>()
    private val deleteObserver = MutableLiveData<Resource<Boolean>>()
    private val bettingTipsObserver = MutableLiveData<Resource<List<BettingTip>>>()


    fun validate(validate: Boolean) {
        fieldValidatorObserver.value = validate
    }

    fun getBettingTips(sport: Sport, upcoming:Boolean) {
        viewModelScope.launch {
            bettingTipsRepository.getBettingTipsBySport(sport,upcoming)
                .catch { exception ->
                    Timber.e(exception)
                    bettingTipsObserver.value = Resource.error(exception.message, null)
                }
                .collect { items ->
                    bettingTipsObserver.value = Resource.success(null, items)
                }
        }
    }

    fun observeValidation() = fieldValidatorObserver

    fun observeForSavedTip() = saveObserver

    fun observeDeletedTip() = deleteObserver

    fun observeOnOlderTips() = olderTipsObserver

    fun observeTips() = bettingTipsObserver
}