/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package com.twoplaytech.drbetting.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.google.firebase.firestore.Query
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.common.FirestoreQueryLiveData
import com.twoplaytech.drbetting.data.Sport
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:27 PM

*/
val colors =
    listOf(R.color.blue, R.color.orange, R.color.seagreen, R.color.cyan, R.color.violet)

fun String.checkImageExtension(): Boolean {
    return this.endsWith(".jpg", true) ||
            this.endsWith(".jpeg", true) ||
            this.endsWith(".png", true) ||
            this.endsWith(".gif", true)
}
fun Query.asFirestoreQueryLiveData(): FirestoreQueryLiveData {
    return FirestoreQueryLiveData(this)
}
fun older(): Date {
    val calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    return calendar.time
}
fun Sport.getSportPlaceHolder(): Int {
    return when (this) {
        Sport.FOOTBALL -> R.drawable.soccer_ball
        Sport.BASKETBALL -> R.drawable.basketball_ball
        Sport.TENNIS -> R.drawable.tennis_ball
        Sport.HANDBALL -> R.drawable.handball_ball
        Sport.VOLLEYBALL -> R.drawable.volleyball_ball
        else -> R.drawable.soccer_ball
    }
}
fun Sport.getSportColor(): Int {
    return when (this) {
        Sport.FOOTBALL -> R.color.blue
        Sport.BASKETBALL -> R.color.orange
        Sport.TENNIS -> R.color.seagreen
        Sport.HANDBALL -> R.color.cyan
        Sport.VOLLEYBALL -> R.color.violet
        else -> R.color.blue
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Sport.getSportDrawable(context: Context): Drawable? {
    return when (this) {
        Sport.FOOTBALL -> context.getDrawable(R.drawable.gradient_blue)
        Sport.BASKETBALL -> context.getDrawable(R.drawable.gradient_orange)
        Sport.TENNIS -> context.getDrawable(R.drawable.gradient_green)
        Sport.HANDBALL -> context.getDrawable(R.drawable.gradient_cyan)
        Sport.VOLLEYBALL -> context.getDrawable(R.drawable.gradient_violet)
        else -> context.getDrawable(R.drawable.gradient_blue)
    }
}
fun Sport.getSportResource():Int{
    return when (this) {
        Sport.FOOTBALL -> R.drawable.gradient_blue
        Sport.BASKETBALL -> R.drawable.gradient_orange
        Sport.TENNIS -> R.drawable.gradient_green
        Sport.HANDBALL -> R.drawable.gradient_cyan
        Sport.VOLLEYBALL -> R.drawable.gradient_violet
        else -> R.drawable.gradient_blue
    }
}
fun Date.convertDateToStringFormat(): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    return simpleDateFormat.format(this)
}

fun Date.getTime(): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    return simpleDateFormat.format(this)
}

fun today(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}
