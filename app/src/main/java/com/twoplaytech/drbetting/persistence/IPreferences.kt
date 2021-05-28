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

package com.twoplaytech.drbetting.persistence

import android.security.keystore.KeyGenParameterSpec

/*
    Author: Damjan Miloshevski 
    Created on 3/29/21 12:03 PM
*/
interface IPreferences {
    companion object{
        val KEY_IS_FIRST_APP_LAUNCH: String = "KEY_IS_FIRST_APP_LAUNCH"
        val KEY_DARK_MODE = "KEY_DARK_MODE"
        val KEY_LOCALE = "KEY_LOCALE"
        val KEY_LOGGED_IN = "KEY_LOGGED_IN"
        val KEY_USER_CREDENTIALS = "KEY_USER_CREDENTIALS"
        val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
    }
    val keyGenParameterSpec: KeyGenParameterSpec
    val masterKeyAlias: String
    fun saveString(key: String, value: String)
    fun saveBoolean(key:String,value:Boolean)
    fun saveInteger(key: String,value:Int)
    fun getString(key: String): String?
    fun getBoolean(key:String):Boolean
    fun getInteger(key: String):Int
}


