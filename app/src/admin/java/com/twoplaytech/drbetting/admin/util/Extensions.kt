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

package com.twoplaytech.drbetting.admin.util

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.twoplaytech.drbetting.R

/*
    Author: Damjan Miloshevski 
    Created on 4/16/21 12:55 PM
*/
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun CharSequence?.isValidPasswordFormat(): Boolean {
    return if (this.isNullOrEmpty()) false
    else this.toString().length >= 6
}

fun Context.dispatchCredentialsDialog(callback: (shouldSaveCredentials: Boolean) -> Unit) {
    MaterialDialog(this).show {
        cancelable(false)
        title(R.string.save_credentials_title_msg)
        message(R.string.save_credentials_msg)
        positiveButton(android.R.string.ok, null) {
            callback.invoke(true)
        }
        negativeButton(R.string.dont_save_option) {
            callback.invoke(false)
        }
    }
}