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
import com.google.firebase.Timestamp
import com.twoplaytech.drbetting.common.Mapify
import com.twoplaytech.drbetting.util.Constants.BETTING_TIP
import com.twoplaytech.drbetting.util.Constants.GAME_TIME
import com.twoplaytech.drbetting.util.Constants.ID
import com.twoplaytech.drbetting.util.Constants.LEAGUE_NAME
import com.twoplaytech.drbetting.util.Constants.RESULT
import com.twoplaytech.drbetting.util.Constants.SPORT
import com.twoplaytech.drbetting.util.Constants.STATUS
import com.twoplaytech.drbetting.util.Constants.TEAM_AWAY
import com.twoplaytech.drbetting.util.Constants.TEAM_HOME
import kotlinx.android.parcel.Parcelize
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:26 PM

*/
@Parcelize
data class BettingType(
    var leagueName: String = "",
    var teamHome: Team? = null,
    var teamAway: Team? = null,
    var gameTime: Date? = null,
    var bettingType: String = "",
    var status: TypeStatus? = null,
    var result: String = "",
    var id: String = "",
    var sport: Sport? = null
) : Parcelable,Mapify {
    constructor(data: Map<String, Any>) : this() {
         id = if  (data.containsKey(ID)) {
            data[ID] as String
        } else {
            ""
        }
        leagueName = if (data.containsKey(LEAGUE_NAME)) {
            data[LEAGUE_NAME] as String
        } else {
            ""
        }
        teamHome = if (data.containsKey(TEAM_HOME)) {
            val teamHomeMap = data[TEAM_HOME] as Map<*, *>
            Team(teamHomeMap)
        } else {
            null
        }
        teamAway = if (data.containsKey(TEAM_AWAY)) {
            val teamAwayMap = data[TEAM_AWAY] as Map<*, *>
            Team(teamAwayMap)
        } else {
            null
        }
        bettingType = if (data.containsKey(BETTING_TIP)) {
            data[BETTING_TIP] as String
        } else {
            ""
        }
        if (data.containsKey(GAME_TIME)) {
            val timestamp = data[GAME_TIME] as Timestamp
            gameTime = timestamp.toDate()
        } else {
            null
        }
        result = if (data.containsKey(RESULT)) {
            data[RESULT] as String
        } else {
            "N/A"
        }
        if (data.containsKey(STATUS) && data[STATUS] != null) {
            val statusType = data[STATUS] as String
            status = getStatus(statusType)
        }
        if (data.containsKey(SPORT) && data[SPORT] != null) {
            val sportType = data[SPORT] as String
            sport = getSport(sportType.toInt())
        }
    }

    private fun getSport(sport: Int): Sport {
        return when (sport) {
            0 -> Sport.FOOTBALL
            1 -> Sport.BASKETBALL
            2 -> Sport.TENNIS
            3 -> Sport.HANDBALL
            4 -> Sport.VOLLEYBALL
            else -> Sport.FOOTBALL
        }
    }

    private fun getStatus(status: String): TypeStatus {
        return when (status) {
            "0" -> TypeStatus.UNKNOWN
            "1" -> TypeStatus.WON
            "2" -> TypeStatus.LOST
            else -> TypeStatus.UNKNOWN
        }
    }

    override fun mapify(): Map<String, Any> {
        val gameTime =
            this.gameTime?.let { Timestamp(it) } ?: Timestamp(Calendar.getInstance().time)
        val status = this.status?.let { it.value } ?: TypeStatus.UNKNOWN.value
        val sport = this.sport?.let { it.value } ?: Sport.FOOTBALL.value
        val teamHome =
            this.teamHome?.let { it.mapify() } ?: mapOf("name" to "teamHome", "logo" to "")
        val teamAway =
            this.teamAway?.let { it.mapify() } ?: mapOf("name" to "teamAway", "logo" to "")
        return mapOf(
            "id" to this.id,
            "leagueName" to this.leagueName,
            "gameTime" to gameTime,
            "result" to this.result,
            "bettingTip" to this.bettingType,
            "sport" to sport,
            "status" to status,
            "teamAway" to teamAway,
            "teamHome" to teamHome
        )
    }
}

