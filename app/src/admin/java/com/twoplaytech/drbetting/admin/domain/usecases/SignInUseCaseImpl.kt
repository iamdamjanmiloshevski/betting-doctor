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

package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.admin.data.models.Credentials
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.admin.data.models.UserInput
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 16:04
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class SignInUseCaseImpl @Inject constructor(
    repository: Repository
) : UseCase(repository), SignInUseCase {

    override fun isAlreadyLoggedIn(callback: (Boolean) -> Unit) {
        repository.isAlreadyLoggedIn {
            callback.invoke(it)
        }
    }
    override fun saveLogin(shouldStayLoggedIn: Boolean) {
        repository.saveLogin(shouldStayLoggedIn)
    }

    override fun saveUserCredentials(email: String, password: String) {
        repository.saveUserCredentials(email,password)
    }

    override fun retrieveUserCredentials(
        onSuccess: (Credentials) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        repository.retrieveUserCredentials(onSuccess = {
            onSuccess.invoke(it)
        },onError = {
            onError.invoke(it)
        })
    }
}