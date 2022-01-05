package com.twoplaytech.drbetting.sportsanalyst.util

import com.twoplaytech.drbetting.util.toZonedDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 5.1.22 11:05
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
fun String.beautifyDate(): String {
    val zonedDate = this.toZonedDate()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return zonedDate.format(dateFormatter)
}

fun today():String{
    val calendar = Calendar.getInstance()
    return calendar.toServerFormatDate()
}

fun Calendar.toServerFormatDate():String{
    val formatter = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
    return formatter.format(this.time)
}
