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

package com.twoplaytech.drbetting.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.repository.Repository
import com.twoplaytech.drbetting.domain.usecases.AppLaunchUseCase
import com.twoplaytech.drbetting.domain.usecases.ChangeThemeUseCase
import com.twoplaytech.drbetting.domain.usecases.GetBettingTipsUseCase
import com.twoplaytech.drbetting.network.resources.BettingTips
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val getBettingTipsUseCase: GetBettingTipsUseCase,
    private val appLaunchUseCase: AppLaunchUseCase,
    private val themeUseCase: ChangeThemeUseCase,
    private val repository: Repository
) :
    ViewModel() {
    private val bettingTipsObserver = MutableLiveData<Resource<Any>>()
    private val appLaunchObserver = MutableLiveData<Int>()
    private val bettingTips: MutableStateFlow<List<BettingTip>> = MutableStateFlow(emptyList())
    val _bettingTIps = bettingTips

    fun getBettingTips1(sport: Sport, upcoming: Boolean) {
        viewModelScope.launch {
            repository.getBettingTipsBySport(sport, false).onStart {

            }.catch { e ->
                Timber.e(e)
            }.collectLatest { either ->
                when (either) {
                    is Either.Failure -> {
                        Timber.e("Response" + either.message.message)
                    }
                    is Either.Response -> {
                        Timber.e("Response ${either.data.size}")
                    }
                }
            }
        }
    }

    fun getBettingTips(sport: Sport, upcoming: Boolean) {
        getBettingTipsUseCase.getBettingTipsBySport(sport, upcoming, onSuccess = { bettingTips ->
            bettingTipsObserver.postValue(Resource.success(null, bettingTips))
        }, onError = { message ->
            bettingTipsObserver.postValue(Resource.error(message.message, null))
        })
    }

    fun getAppLaunchCount() {
        appLaunchUseCase.getAppLaunchesCount {
            appLaunchObserver.value = it
        }
    }

    fun incrementAppLaunch() {
        appLaunchUseCase.incrementAppLaunch()
    }

    private val appThemeObserver = MutableLiveData<Int>()
    fun changeTheme(appTheme: Int) {
        themeUseCase.saveAppTheme(appTheme)
        appThemeObserver.value = appTheme
    }

    fun getAppTheme() {
        themeUseCase.getAppTheme {
            appThemeObserver.value = it
        }
    }

    fun observeAppTheme() = appThemeObserver
    fun observeAppLaunch() = appLaunchObserver
    fun observeTips() = bettingTipsObserver
}