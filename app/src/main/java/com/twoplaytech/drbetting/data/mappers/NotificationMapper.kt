package com.twoplaytech.drbetting.data.mappers

import com.twoplaytech.drbetting.data.models.Notification

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 15:40
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface NotificationMapper {
    fun toNotification(map:Map<String,String>): Notification
}