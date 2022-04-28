package com.twoplaytech.drbetting.network.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

/*
    Author: Damjan Miloshevski 
    Created on 27.4.22 15:15
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Serializable
@Resource("betting-tips")
class BettingTips{
    @Serializable
    @Resource("{id}")
    class Id(val parent:BettingTips = BettingTips(), val id:String)
    @Serializable
    @Resource("{sport}")
    class Sport(val parent:BettingTips = BettingTips(),val sport:String){
        @Serializable
        @Resource("upcoming")
        class Upcoming(val parent:Sport)
        @Serializable
        @Resource("older")
        class Older(val parent:Sport)
    }
}


