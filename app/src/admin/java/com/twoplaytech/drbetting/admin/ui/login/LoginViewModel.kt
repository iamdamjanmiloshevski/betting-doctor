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

package com.twoplaytech.drbetting.admin.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.Resource
import com.twoplaytech.drbetting.admin.repository.FirebaseRepository
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_LOGGED_IN
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_USER_CREDENTIALS
import com.twoplaytech.drbetting.persistence.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 4/7/21 2:57 PM
*/
@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: FirebaseRepository,
    private val preferencesManager: SharedPreferencesManager
) :
    ViewModel() {
    private val loginObserver: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val loginEnabledObserver = MutableLiveData<Boolean>()
    private val alreadyLoggedIn = MutableLiveData<Boolean>()
    private val credentialsObserver = MutableLiveData<Resource<Pair<String, String>>>()

    fun login(email: String, password: String) {
        loginObserver.value = Resource.loading(context.getString(R.string.signing_in_msg), null)
        repository.signIn(email, password, callback = { loginStatus, errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                loginObserver.value = Resource.error(errorMessage, null)
            } else loginObserver.value = Resource.success(null, loginStatus)
        })
    }

    fun logout() {
        repository.logout {
            saveLogin(false)
            loginObserver.value = Resource.success(null, false)
        }
    }

    fun enableLogin(enable: Boolean) {
        loginEnabledObserver.value = enable
    }

    fun checkIfUserIsAlreadySignedIn() {
        val user = repository.getUser()
        val shouldKeepUserSignedIn = preferencesManager.getBoolean(KEY_LOGGED_IN)
        alreadyLoggedIn.value = user != null && shouldKeepUserSignedIn
    }

    fun retrieveCredentials() {
        val json = preferencesManager.getString(KEY_USER_CREDENTIALS)
        if (json.isNullOrEmpty()) {
            credentialsObserver.value = Resource.error(null, null)
        } else {
            credentialsObserver.value = Resource.success(null, json.deserializeCredentials())
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
        val jsonObject = JSONObject()
        jsonObject.put(KEY_EMAIL, email)
        jsonObject.put(KEY_PASSWORD, password)
        preferencesManager.saveString(
            KEY_USER_CREDENTIALS,
            jsonObject.toString()
        )
    }

    private fun String.deserializeCredentials(): Pair<String, String> {
        val jsonObject = JSONObject(this)
        val email = jsonObject.getString(KEY_EMAIL)
        val password = jsonObject.getString(KEY_PASSWORD)
        return Pair(email, password)
    }

    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
    }
}