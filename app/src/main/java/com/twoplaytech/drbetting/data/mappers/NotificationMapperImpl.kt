package com.twoplaytech.drbetting.data.mappers

import com.twoplaytech.drbetting.data.models.Notification


/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 15:41
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
object NotificationMapperImpl: NotificationMapper {
    override fun toNotification(map: Map<String, String>): Notification {
        val topic = map["topic"] as String
        val notificationMessage = map["message"] as String
        val channel = map["channel"] as String
        val channelId = map["channelId"] as String
       return Notification(topic,notificationMessage, channel,channelId)
    }
}