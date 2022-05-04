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
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.admin.domain.usecases.AppLaunchUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.ChangeThemeUseCase
import com.twoplaytech.drbetting.admin.ui.common.BettingTipsUiState
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.domain.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM

*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(
    private val appLaunchUseCase: AppLaunchUseCase,
    private val themeUseCase: ChangeThemeUseCase,
    private val repository: Repository
) :
    ViewModel() {
    private val appLaunchObserver = MutableLiveData<Int>()
    private val _bettingTipsState: MutableStateFlow<BettingTipsUiState> = MutableStateFlow(BettingTipsUiState.Loading)
    val bettingTipsState: StateFlow<BettingTipsUiState> = _bettingTipsState

    fun getBettingTips(sport: Sport, upcoming: Boolean) {
        viewModelScope.launch {
            repository.getBettingTipsBySport(sport, upcoming).onStart {
                _bettingTipsState.value = BettingTipsUiState.Loading
            }.catch { e ->
                Timber.e(e)
                _bettingTipsState.value = BettingTipsUiState.Error(e)
            }.collectLatest { either ->
                when (either) {
                    is Either.Failure -> {
                        _bettingTipsState.value = BettingTipsUiState.Error(Throwable(either.message.message))
                    }
                    is Either.Response -> {
                        _bettingTipsState.value = BettingTipsUiState.Success(either.data)
                    }
                }
            }
        }
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
}