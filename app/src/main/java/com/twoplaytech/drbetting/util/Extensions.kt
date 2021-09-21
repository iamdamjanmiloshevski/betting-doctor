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

package com.twoplaytech.drbetting.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.TypeStatus
import com.twoplaytech.drbetting.databinding.DialogDisclaimerBinding
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*


/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:27 PM

*/
val colors =
    listOf(R.color.blue, R.color.orange, R.color.seagreen, R.color.cyan, R.color.violet)

fun String.checkImageExtension(): Boolean {
    return this.endsWith(".jpg", true) ||
            this.endsWith(".jpeg", true) ||
            this.endsWith(".png", true) ||
            this.endsWith(".svg") ||
            this.endsWith(".gif", true)
}


fun older(): Date {
    val calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR_OF_DAY, -1)
    return calendar.time
}

fun Sport.getSportPlaceHolder(): Int {
    return when (this) {
        Sport.Football -> R.drawable.soccer_ball
        Sport.Basketball -> R.drawable.basketball_ball
        Sport.Tennis -> R.drawable.tennis_ball
        Sport.Handball -> R.drawable.handball_ball
        Sport.Volleyball -> R.drawable.volleyball_ball
        else -> R.drawable.soccer_ball
    }
}

fun String.getSportPlaceHolder(): Int {
    return when (this) {
        "Football" -> R.drawable.soccer_ball
        "Basketball" -> R.drawable.basketball_ball
        "Tennis" -> R.drawable.tennis_ball
        "Handball" -> R.drawable.handball_ball
        "Volleyball" -> R.drawable.volleyball_ball
        else -> R.drawable.soccer_ball
    }
}

fun Sport.getSportColor(): Int {
    return when (this) {
        Sport.Football -> R.color.blue
        Sport.Basketball -> R.color.orange
        Sport.Tennis -> R.color.seagreen
        Sport.Handball -> R.color.cyan
        Sport.Volleyball -> R.color.violet
        else -> R.color.blue
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Sport.getSportDrawable(context: Context): Drawable? {
    return when (this) {
        Sport.Football -> ContextCompat.getDrawable(context, R.drawable.gradient_blue)
        Sport.Basketball -> ContextCompat.getDrawable(context, R.drawable.gradient_orange)
        Sport.Tennis -> ContextCompat.getDrawable(context, R.drawable.gradient_green)
        Sport.Handball -> ContextCompat.getDrawable(context, R.drawable.gradient_cyan)
        Sport.Volleyball -> ContextCompat.getDrawable(context, R.drawable.gradient_violet)
        else -> ContextCompat.getDrawable(context, R.drawable.gradient_blue)
    }
}

fun Sport.getSportResource(): Int {
    return when (this) {
        Sport.Football -> R.drawable.gradient_blue
        Sport.Basketball -> R.drawable.gradient_orange
        Sport.Tennis -> R.drawable.gradient_green
        Sport.Handball -> R.drawable.gradient_cyan
        Sport.Volleyball -> R.drawable.gradient_violet
        else -> R.drawable.gradient_blue
    }
}

fun Int.getSportFromIndex(): Sport {
    return when (this) {
        0 -> Sport.Football
        1 -> Sport.Basketball
        2 -> Sport.Tennis
        3 -> Sport.Handball
        4 -> Sport.Volleyball
        else -> Sport.Football
    }
}

fun Date.convertDateToStringFormat(): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    return simpleDateFormat.format(this)
}

fun Date.getTime(): String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    return simpleDateFormat.format(this)
}

fun today(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}

fun String.beautifyGameDate(): String {
    val zonedDateTime = this.toZonedDate()
    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    return zonedDateTime.format(dateFormatter)
}

fun String.beautifyGameTime(context: Context): String {
    val zonedDateTime = this.toZonedDate()
    val is24hrFormat = DateFormat.is24HourFormat(context)
    val pattern = if (!is24hrFormat) "h:mm a z" else "H:mm z"
    val dateFormatter = DateTimeFormatter.ofPattern(pattern)
    return zonedDateTime.format(dateFormatter)
}

fun String.toZonedDate(): ZonedDateTime {
    // Sep 21, 2021, 7:30:00 PM
    this.toDate()?.let {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.time), ZoneId.of("UTC")).withZoneSameInstant(
            ZoneId.systemDefault())
    } ?: throw NullPointerException("Date must not be null!")
}

fun String.toDate():Date{
    val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.getDefault())
    return simpleDateFormat.parse(this)
}
fun Date.toStringDate():String{
    val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.getDefault())
    return simpleDateFormat.format(this)
}
fun Context.getRandomBackground(): Pair<Drawable?, Int> {
    val random = Random()
    val indexes = IntArray(5)
    val idx = random.nextInt(indexes.size)
    val sports = listOf(
        Sport.Football.getSportDrawable(this),
        Sport.Basketball.getSportDrawable(this),
        Sport.Tennis.getSportDrawable(this),
        Sport.Handball.getSportDrawable(this),
        Sport.Volleyball.getSportDrawable(this),
    )
    val colors = listOf(
        R.color.blue,
        R.color.orange,
        R.color.seagreen,
        R.color.cyan,
        R.color.violet
    )

    return Pair(sports[idx], colors[idx])
}

fun getRandomBackground(): Pair<Int, Int> {
    val random = Random()
    val indexes = IntArray(5)
    val idx = random.nextInt(indexes.size)
    val sports = listOf(
        R.drawable.gradient_blue,
        R.drawable.gradient_orange,
        R.drawable.gradient_green,
        R.drawable.gradient_cyan,
        R.drawable.gradient_violet,
    )
    val colors = listOf(
        R.color.blue,
        R.color.orange,
        R.color.seagreen,
        R.color.cyan,
        R.color.violet
    )
    return Pair(sports[idx], colors[idx])
}

fun Context.showDisclaimer() {
    val dialog = AlertDialog.Builder(this)
    dialog.setCancelable(false)
    dialog.setTitle(R.string.item_disclaimer)
    val inflater = LayoutInflater.from(this)
    val dialogBinding = DialogDisclaimerBinding.inflate(inflater)
    dialog.setView(dialogBinding.root)
    dialog.setPositiveButton(android.R.string.ok) { disclaimerDialog, _ ->
        disclaimerDialog.dismiss()
    }
    dialogBinding.webView.loadUrl("https://betting-tips-2-odds.firebaseapp.com/disclaimer.html")
    dialog.show()
}

fun Context.writeCopyright(): String {
    val dateFormatter = SimpleDateFormat("YYYY")
    val calendar = Calendar.getInstance()
    val date = dateFormatter.format(calendar.time)
    return this.getString(R.string.copyright, date)
}

fun Context.getVersionName(): String? {
    return try {
        val pInfo: PackageInfo =
            this.packageManager.getPackageInfo(this.getPackageName(), 0)
        pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        null
    }
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun FragmentActivity.restartApp() {
    val i = this.packageManager
        .getLaunchIntentForPackage(baseContext.packageName)
    i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(i)
    this.finishAffinity()
}

fun <T : Activity> Activity.startActivityWithClearTask(destination: Class<T>) {
    val intent = Intent(this, destination)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    this.startActivity(intent)
    this.finishAffinity()
}

fun String.toStatus(): TypeStatus {
    return when (this) {
        "UNKNOWN" -> TypeStatus.UNKNOWN
        "LOST" -> TypeStatus.LOST
        "WON" -> TypeStatus.WON
        else -> throw Exception("Unknown type status")
    }
}

