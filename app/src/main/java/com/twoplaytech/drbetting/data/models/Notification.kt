package com.twoplaytech.drbetting.data.models

data class Notification(val topic:String, val message:String = "", val channel:String = "", val channelId:String = "")