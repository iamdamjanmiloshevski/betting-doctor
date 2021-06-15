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

package com.twoplaytech.drbetting.data

import android.os.Parcelable
import com.twoplaytech.drbetting.common.Mapify
import com.twoplaytech.drbetting.util.Constants.TEAM_LOGO
import com.twoplaytech.drbetting.util.Constants.TEAM_NAME
import com.twoplaytech.drbetting.util.checkImageExtension
import kotlinx.android.parcel.Parcelize

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:26 PM

*/
@Parcelize
data class Team(var name: String = "", var logo: String = "") : Parcelable,Mapify {
    constructor(data: Map<*, *>) : this() {
        name = if (data.containsKey(TEAM_NAME)
            && data[TEAM_NAME] != null) {
            data[TEAM_NAME] as String
        } else {
            ""
        }
        logo = if (data.containsKey(TEAM_LOGO)
            && data[TEAM_LOGO] != null) {
            if (!(data[TEAM_LOGO] as String).checkImageExtension()) {
                ""
            } else {
                data[TEAM_LOGO] as String
            }
        } else {
            ""
        }
    }
    override fun mapify():Map<String,Any>{
        return mapOf(
            "name" to this.name,
            "logo" to this.logo
        )
    }
}
