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
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.usecases.AppLaunchUseCase
import com.twoplaytech.drbetting.domain.usecases.ChangeThemeUseCase
import com.twoplaytech.drbetting.domain.usecases.GetBettingTipsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val getBettingTipsUseCase: GetBettingTipsUseCase,
    private val appLaunchUseCase: AppLaunchUseCase,
    private val themeUseCase: ChangeThemeUseCase
) :
    ViewModel() {
    private val bettingTipsObserver = MutableLiveData<Resource<Any>>()
    private val appLaunchObserver = MutableLiveData<Int>()

    fun getBettingTips(sport: Sport, upcoming: Boolean) {
        getBettingTipsUseCase.getBettingTipsBySport(sport, upcoming, onSuccess = { bettingTips ->
            bettingTipsObserver.postValue(Resource.success(null, bettingTips))
        }, onError = { message ->
            bettingTipsObserver.postValue(Resource.error(message.message,null ))
        })
    }

    fun getAppLaunchCount(){
        appLaunchUseCase.getAppLaunchesCount {
            appLaunchObserver.value = it
        }
    }
    fun incrementAppLaunch(){
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