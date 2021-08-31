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

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.data.Credentials
import com.twoplaytech.drbetting.data.entities.AccessToken
import com.twoplaytech.drbetting.data.entities.UserInput
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.usecases.SignInUseCase
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_LOGGED_IN
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_USER_CREDENTIALS
import com.twoplaytech.drbetting.persistence.SharedPreferencesManager
import com.twoplaytech.drbetting.util.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 4/7/21 2:57 PM
*/
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val preferencesManager: SharedPreferencesManager
) :
    ViewModel() {
    private val loginObserver: MutableLiveData<Resource<AccessToken>> = MutableLiveData()
    private val loginEnabledObserver = MutableLiveData<Boolean>()
    private val alreadyLoggedIn = MutableLiveData<Boolean>()
    private val credentialsObserver = MutableLiveData<Resource<Credentials>>()

    fun login(context: Context, email: String, password: String) {
        loginObserver.value = Resource.loading(context.getString(R.string.signing_in_msg), null)
        val userInput = UserInput(email = email, password = password)
        signInUseCase.signIn(userInput,onSuccess = {accessToken ->
            loginObserver.postValue(Resource.success(null, accessToken))
        },onError = {cause->
            loginObserver.postValue( Resource.error(cause.message, null))
        })
    }

    fun logout() {

    }

    fun enableLogin(enable: Boolean) {
        loginEnabledObserver.value = enable
    }

    fun retrieveCredentials() {
        val credentialsJson = preferencesManager.getString(KEY_USER_CREDENTIALS)
        if (credentialsJson.isNullOrEmpty()) {
            credentialsObserver.value = Resource.error(null, null)
        } else {
            credentialsObserver.value = Resource.success(null, GsonUtil.fromJson(credentialsJson))
        }
    }

    fun observeLoginEnabled() = loginEnabledObserver
    fun observeLogin() = loginObserver
    fun observeAlreadyLoggedIn() = alreadyLoggedIn
    fun observeForCredentials() = credentialsObserver


    fun saveLogin(shouldStayLoggedIn: Boolean) {
        preferencesManager.saveBoolean(KEY_LOGGED_IN, shouldStayLoggedIn)
    }

    fun saveUserCredentials(email: String, password: String) {
        preferencesManager.saveString(
            KEY_USER_CREDENTIALS,
            GsonUtil.toJson(Credentials(email,password))
        )
    }
}