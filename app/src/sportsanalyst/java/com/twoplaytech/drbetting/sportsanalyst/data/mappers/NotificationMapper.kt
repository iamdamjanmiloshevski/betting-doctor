package com.twoplaytech.drbetting.sportsanalyst.data.mappers

import com.twoplaytech.drbetting.sportsanalyst.data.Notification

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 15:40
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface NotificationMapper {
    fun toNotification(map:Map<String,String>):Notification
}