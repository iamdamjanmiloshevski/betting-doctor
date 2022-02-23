package com.twoplaytech.drbetting.data.models

import androidx.annotation.Keep

/*
    Author: Damjan Miloshevski 
    Created on 21.9.21 15:48
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Keep
data class BettingTipInput(
    var leagueName: String,
    var teamHome: Team?,
    var teamAway: Team?,
    var gameTime: String?,
    var bettingType: String,
    var status: String,
    var result: String,
    var _id: String?,
    var sport: String,
    val coefficient:String? = null,
    val ticketId:String? = null
)