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
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.firestore.Query
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.common.FirestoreQueryLiveData
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Sport2
import com.twoplaytech.drbetting.databinding.DialogDisclaimerBinding
import timber.log.Timber
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

fun Query.asFirestoreQueryLiveData(): FirestoreQueryLiveData {
    return FirestoreQueryLiveData(this)
}

fun older(): Date {
    val calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR_OF_DAY, -1)
    return calendar.time
}

fun Sport.getSportPlaceHolder(): Int {
    return when (this) {
        Sport.FOOTBALL -> R.drawable.soccer_ball
        Sport.BASKETBALL -> R.drawable.basketball_ball
        Sport.TENNIS -> R.drawable.tennis_ball
        Sport.HANDBALL -> R.drawable.handball_ball
        Sport.VOLLEYBALL -> R.drawable.volleyball_ball
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
        Sport.FOOTBALL -> R.color.blue
        Sport.BASKETBALL -> R.color.orange
        Sport.TENNIS -> R.color.seagreen
        Sport.HANDBALL -> R.color.cyan
        Sport.VOLLEYBALL -> R.color.violet
        else -> R.color.blue
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Sport.getSportDrawable(context: Context): Drawable? {
    return when (this) {
        Sport.FOOTBALL -> ContextCompat.getDrawable(context, R.drawable.gradient_blue)
        Sport.BASKETBALL -> ContextCompat.getDrawable(context, R.drawable.gradient_orange)
        Sport.TENNIS -> ContextCompat.getDrawable(context, R.drawable.gradient_green)
        Sport.HANDBALL -> ContextCompat.getDrawable(context, R.drawable.gradient_cyan)
        Sport.VOLLEYBALL -> ContextCompat.getDrawable(context, R.drawable.gradient_violet)
        else -> ContextCompat.getDrawable(context, R.drawable.gradient_blue)
    }
}

fun Sport.getSportResource(): Int {
    return when (this) {
        Sport.FOOTBALL -> R.drawable.gradient_blue
        Sport.BASKETBALL -> R.drawable.gradient_orange
        Sport.TENNIS -> R.drawable.gradient_green
        Sport.HANDBALL -> R.drawable.gradient_cyan
        Sport.VOLLEYBALL -> R.drawable.gradient_violet
        else -> R.drawable.gradient_blue
    }
}
 fun Int.getSportFromIndex():Sport{
    return when(this){
        0 -> Sport.FOOTBALL
        1 -> Sport.BASKETBALL
        2 -> Sport.TENNIS
        3 -> Sport.HANDBALL
        4 -> Sport.VOLLEYBALL
        else -> Sport.FOOTBALL
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

fun Context.getRandomBackground(): Pair<Drawable?, Int> {
    val random = Random()
    val indexes = IntArray(5)
    val idx = random.nextInt(indexes.size)
    val sports = listOf(
        Sport.FOOTBALL.getSportDrawable(this),
        Sport.BASKETBALL.getSportDrawable(this),
        Sport.TENNIS.getSportDrawable(this),
        Sport.HANDBALL.getSportDrawable(this),
        Sport.VOLLEYBALL.getSportDrawable(this),
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
fun Context.writeCopyright():String{
    val dateFormatter = SimpleDateFormat("YYYY")
    val calendar = Calendar.getInstance()
    val date = dateFormatter.format(calendar.time)
    return this.getString(R.string.copyright, date)
}
fun Context.getVersionName():String?{
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
 fun <T:Activity> Activity.startActivityWithClearTask(destination:Class<T>){
    val intent = Intent(this, destination)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    this.startActivity(intent)
    this.finishAffinity()
}

