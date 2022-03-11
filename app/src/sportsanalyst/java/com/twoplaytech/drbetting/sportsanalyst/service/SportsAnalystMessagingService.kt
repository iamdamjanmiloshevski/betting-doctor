package com.twoplaytech.drbetting.sportsanalyst.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.sportsanalyst.data.Notification
import com.twoplaytech.drbetting.sportsanalyst.data.mappers.NotificationMapperImpl
import com.twoplaytech.drbetting.sportsanalyst.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.random.Random

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 14:57
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@AndroidEntryPoint
class SportsAnalystMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        if(!message.data.isNullOrEmpty()){
            sendNotification(NotificationMapperImpl.toNotification(  message.data))
        }
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(notification:Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, notification.channel)
            .setSmallIcon(R.drawable.ic_sports_analyst_notification)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(notification.message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notification.channelId,
                notification.channel,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val random = Random(10000)
        val notificationId = random.nextInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Timber.i("token changed")
    }
}

