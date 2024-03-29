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

package com.twoplaytech.drbetting.ui.util
import com.google.firebase.messaging.FirebaseMessaging
import com.twoplaytech.drbetting.ui.util.Constants.NOTIFICATIONS_TOPIC
import timber.log.Timber

/*
    Author: Damjan Miloshevski 
    Created on 17.9.21 14:55
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
object NotificationsManager {

   private fun subscribeToTopic(topic:String = NOTIFICATIONS_TOPIC){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Timber.e("Successfully subscribed to notifications topic $topic")
        }
    }
   private fun unsubscribeFromTopic(topic: String = NOTIFICATIONS_TOPIC){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener {
            Timber.e("Successfully unsubscribed to notifications topic $topic")
        }
    }
    fun toggleNotifications(toggle:Boolean){
        when(toggle){
            true -> subscribeToTopic()
            false -> unsubscribeFromTopic()
        }
    }
}