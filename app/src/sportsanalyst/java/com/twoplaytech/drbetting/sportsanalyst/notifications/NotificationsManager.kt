package com.twoplaytech.drbetting.sportsanalyst.notifications

import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 08:51
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
object NotificationsManager {
    const val TOPIC_TICKET = "Ticket"
    fun toggleNotifications(enabled:Boolean) {
        if (enabled) {
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_TICKET).addOnSuccessListener {
                Timber.i("Successfully subscribed to notifications topic Ticket")
            }
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_TICKET).addOnSuccessListener {
                Timber.i("Successfully unsubscribed to notifications topic Ticket")
            }
        }
    }
}