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

package com.twoplaytech.drbetting.data.datasource

import com.twoplaytech.drbetting.persistence.SharedPreferencesManager
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 1.9.21 10:35
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class LocalDataSourceImpl @Inject constructor(private val preferences: SharedPreferencesManager) :
    LocalDataSource {
    override fun saveBoolean(key: String, value: Boolean) {
        preferences.saveBoolean(key, value)
    }

    override fun saveString(key: String, value: String) {
        preferences.saveString(key, value)
    }

    override fun saveInt(key: String, value: Int) {
        preferences.saveInteger(key,value)
    }


    override fun getBoolean(key: String, callback: (Boolean) -> Unit) {
        callback.invoke(preferences.getBoolean(key))
    }

    override fun getString(key: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        val response = preferences.getString(key)
        if (response.isNullOrBlank()) onError.invoke(Throwable("null value")) else onSuccess.invoke(
            response
        )
    }

    override fun getString(key: String): String? {
        return preferences.getString(key)
    }

    override fun getInt(key: String, callback: (Int) -> Unit) {
       callback.invoke( preferences.getInteger(key))
    }

}