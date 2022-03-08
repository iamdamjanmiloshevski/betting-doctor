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
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.afollestad.materialdialogs.MaterialDialog
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.DropdownType
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.TypeStatus
import com.twoplaytech.drbetting.util.getSportPlaceHolder
import com.twoplaytech.drbetting.util.getStatusResource
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 4/16/21 12:55 PM
*/

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

fun getRandomBackground(): Pair<Int, Int> {
    val random = Random()
    val indexes = IntArray(5)
    val idx = random.nextInt(indexes.size)
    val sports = listOf(
        R.drawable.gradient_blue,
        R.drawable.gradient_orange,
        R.drawable.gradient_green,
        R.drawable.gradient_cyan,
        R.drawable.gradient_violet,
    )
    val colors = listOf(
        R.color.blue,
        R.color.orange,
        R.color.seagreen,
        R.color.cyan,
        R.color.violet
    )
    return Pair(sports[idx], colors[idx])
}
fun TextView.changeDrawable(@DrawableRes resource:Int){
   this.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,resource, 0)
}
fun Date.beautify():String{
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return sdf.format(this)
}
fun DropdownType.getIconFromTypeFromSelectedItem(
    item: String
): Int {
    val icon = when (this) {
        DropdownType.SPORT -> {
            when (item) {
                "Football" -> Sport.Football.getSportPlaceHolder()
                "Basketball" -> Sport.Basketball.getSportPlaceHolder()
                "Tennis" -> Sport.Tennis.getSportPlaceHolder()
                "Handball" -> Sport.Handball.getSportPlaceHolder()
                "Volleyball" -> Sport.Volleyball.getSportPlaceHolder()
                else -> throw Exception("Unknown sport type")
            }
        }
        DropdownType.STATUS -> {
            when (item) {
                "Unknown" -> TypeStatus.UNKNOWN.getStatusResource()
                "Won" -> TypeStatus.WON.getStatusResource()
                "Lost" -> TypeStatus.LOST.getStatusResource()
                else -> throw Exception("Unknown status type")
            }
        }
    }
    return icon
}

/**
 * @return Returns updated list of BettingTip
 *
 * @param updatedList
 */
fun MutableList<BettingTip>.update(updatedList:List<BettingTip>){
    for(idx in this.indices){
        for(idy in updatedList.indices){
            val item = this[idx]
            val updatedItem = updatedList[idy]
            if((item._id == updatedItem._id) && item != updatedItem){
               this[idx] = updatedItem
            }
        }
    }
}