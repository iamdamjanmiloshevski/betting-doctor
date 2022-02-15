package com.twoplaytech.drbetting.sportsanalyst.util

import com.twoplaytech.drbetting.util.toZonedDate
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 5.1.22 11:05
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
val dateFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd MMM yyyy")
val calendar: Calendar = Calendar.getInstance()

fun String.beautifyDate(): String {
    val zonedDate = this.toZonedDate()
    return zonedDate.format(dateFormatter)
}

fun ZonedDateTime?.beautify(): String {
    return try {
        this?.format(dateFormatter) ?: throw Exception("Unable to format $this into $dateFormatter")
    } catch (e: Exception) {
        Timber.e("Error formatting $this. Exception -> $e")
        ""
    }
}

fun Long.toZonedDate(): ZonedDateTime? {
    return try {
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    } catch (e: Exception) {
        Timber.e("Error parsing $this into ZonedDateTime. Exception -> $e")
        ZonedDateTime.now()
    }
}

fun Long.toServerDate():String{
    calendar.timeInMillis = this
    return calendar.toServerFormatDate()
}

fun Date.format(): String {
    val zonedDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault())
    return zonedDate.format(dateFormatter)
}

fun today(): String {
    return calendar.toServerFormatDate()
}

fun Calendar.toServerFormatDate(): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(this.time)
}
