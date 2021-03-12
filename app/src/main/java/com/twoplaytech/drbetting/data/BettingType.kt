package com.twoplaytech.drbetting.data

import com.google.firebase.Timestamp
import com.twoplaytech.drbetting.util.Constants.BETTING_TIP
import com.twoplaytech.drbetting.util.Constants.GAME_TIME
import com.twoplaytech.drbetting.util.Constants.LEAGUE_NAME
import com.twoplaytech.drbetting.util.Constants.RESULT
import com.twoplaytech.drbetting.util.Constants.SPORT
import com.twoplaytech.drbetting.util.Constants.STATUS
import com.twoplaytech.drbetting.util.Constants.TEAM_AWAY
import com.twoplaytech.drbetting.util.Constants.TEAM_HOME
import java.io.Serializable
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:26 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
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
) : Serializable {
    constructor(data: Map<String, Any>) : this() {
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
        if (data.containsKey(STATUS) &&data[STATUS] != null) {
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
    private fun getStatus(status:String):TypeStatus{
        return when(status){
            "0" -> TypeStatus.UNKNOWN
            "1" -> TypeStatus.WON
            "2" -> TypeStatus.LOST
            else -> TypeStatus.UNKNOWN
        }
    }
}

