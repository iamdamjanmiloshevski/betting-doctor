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

package com.twoplaytech.drbetting.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 12:27
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Keep
data class BettingTip(
    val leagueName: String,
    val teamHome: Team?,
    val teamAway: Team?,
    val gameTime: String,
    val bettingType: String,
    val status: TypeStatus,
    val result: String,
    val _id: String?= null,
    val sport: Sport,
    val coefficient:String? = null,
    val ticketId:String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readParcelable(Team::class.java.classLoader),
        parcel.readParcelable(Team::class.java.classLoader),
        parcel.readString()?: "",
        parcel.readString()  ?: "",
        TypeStatus.valueOf(parcel.readString() ?: "UNKNOWN"),
        parcel.readString()  ?: "",
        parcel.readString() ?: "",
        Sport.valueOf(parcel.readString() ?: "Football"),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(leagueName)
        parcel.writeParcelable(teamHome, flags)
        parcel.writeParcelable(teamAway, flags)
        parcel.writeString(gameTime)
        parcel.writeString(bettingType)
        parcel.writeString(status.name)
        parcel.writeString(result)
        parcel.writeString(_id)
        parcel.writeString(sport.name)
        parcel.writeString(coefficient)
        parcel.writeString(ticketId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BettingTip> {
        override fun createFromParcel(parcel: Parcel): BettingTip {
            return BettingTip(parcel)
        }

        override fun newArray(size: Int): Array<BettingTip?> {
            return arrayOfNulls(size)
        }
    }
}
