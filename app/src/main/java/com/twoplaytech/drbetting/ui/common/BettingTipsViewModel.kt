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
import com.google.firebase.firestore.Query
import com.twoplaytech.drbetting.common.FirestoreQueryLiveData
import com.twoplaytech.drbetting.data.BettingTip
import com.twoplaytech.drbetting.data.BettingTip2
import com.twoplaytech.drbetting.data.Resource
import com.twoplaytech.drbetting.data.Sport2
import com.twoplaytech.drbetting.repository.BettingTipsRepository
import com.twoplaytech.drbetting.repository.FirestoreRepository
import com.twoplaytech.drbetting.util.Constants.GAME_TIME
import com.twoplaytech.drbetting.util.Constants.SPORT
import com.twoplaytech.drbetting.util.asFirestoreQueryLiveData
import com.twoplaytech.drbetting.util.older
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val repository: FirestoreRepository,
    private val bettingTipsRepository: BettingTipsRepository
) :
    ViewModel() {
    private val olderTipsObserver = MutableLiveData<Resource<List<BettingTip>>>()
    private val fieldValidatorObserver = MutableLiveData<Boolean>()
    private val saveObserver = MutableLiveData<Resource<Boolean>>()
    private val deleteObserver = MutableLiveData<Resource<Boolean>>()
    private val bettingTipsObserver = MutableLiveData<Resource<List<BettingTip2>>>()

    fun getUpcomingTips(type: String): FirestoreQueryLiveData {
        return repository.getBettingTips()
            .whereEqualTo(SPORT, type)
            .whereGreaterThanOrEqualTo(GAME_TIME, Calendar.getInstance().time)
            .orderBy(GAME_TIME, Query.Direction.DESCENDING)
            .asFirestoreQueryLiveData()
    }

    fun getOlderTips(type: String) {
        olderTipsObserver.value = Resource.loading(null, null)
        val olderTips = mutableListOf<BettingTip>()
        olderTips.clear()
        repository.getBettingTips()
            .whereEqualTo(SPORT, type)
            .whereLessThanOrEqualTo(GAME_TIME, older())
            .orderBy(GAME_TIME, Query.Direction.DESCENDING)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val bettingTip = BettingTip(document.data)
                    if (!olderTips.contains(bettingTip)) {
                        olderTips.add(bettingTip)
                    }
                }
            }.addOnCompleteListener { task ->
                if (task.isComplete) {
                    if (olderTips.isNotEmpty()) {
                        olderTipsObserver.value = Resource.success(null, olderTips)
                    } else {
                        olderTipsObserver.value = Resource.success("No data", listOf())
                    }
                }
            }.addOnFailureListener { exception ->
                olderTipsObserver.value = Resource.error(exception.localizedMessage, null)
            }
    }

    fun validate(validate: Boolean) {
        fieldValidatorObserver.value = validate
    }

    fun getBettingTips(sport2: Sport2,upcoming:Boolean) {
        viewModelScope.launch {
            bettingTipsRepository.getBettingTipsBySport(sport2,upcoming)
                .catch { exception ->
                    Timber.e(exception)
                    bettingTipsObserver.value = Resource.error(exception.message, null)
                }
                .collect { items ->
                    bettingTipsObserver.value = Resource.success(null, items)
                }
        }
    }

    fun saveBettingTip(bettingTip: BettingTip, shouldUpdate: Boolean = false) {
        if (shouldUpdate) {
            repository.updateBettingTip(bettingTip, successCallback = {
                saveObserver.value = Resource.success(it, true)
            }, failureCallback = {
                saveObserver.value = Resource.error(it, false)
            })
        } else {
            repository.saveBettingTip(bettingTip, successCallback = {
                saveObserver.value = Resource.success(it, true)
            }, failureCallback = {
                saveObserver.value = Resource.error(it, false)
            })
        }
    }

    fun deleteBettingTip(bettingTip: BettingTip) {
        repository.deleteBettingTip(bettingTip, successCallback = {
            deleteObserver.value = Resource.success(it, true)
        }, failureCallback = {
            deleteObserver.value = Resource.error(it, false)
        })
    }

    fun observeValidation() = fieldValidatorObserver

    fun observeForSavedTip() = saveObserver

    fun observeDeletedTip() = deleteObserver

    fun observeOnOlderTips() = olderTipsObserver

    fun observeTips() = bettingTipsObserver
}