package com.twoplaytech.drbetting.sportsanalyst.util

import com.twoplaytech.drbetting.util.toZonedDate
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 5.1.22 11:05
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
val dateFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd MMM yyyy")
fun String.beautifyDate(): String {
    val zonedDate = this.toZonedDate()
    return zonedDate.format(dateFormatter)
}

fun Date.format(): String {
    val zonedDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    return zonedDate.format(dateFormatter)
}

fun today(): String {
    val calendar = Calendar.getInstance()
    return calendar.toServerFormatDate()
}

fun Calendar.toServerFormatDate(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(this.time)
}
