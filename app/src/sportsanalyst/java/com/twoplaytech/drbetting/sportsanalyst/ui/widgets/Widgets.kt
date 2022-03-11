package com.twoplaytech.drbetting.sportsanalyst.ui.widgets

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.twoplaytech.drbetting.R

/*
    Author: Damjan Miloshevski 
    Created on 14.2.22 09:38
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/

fun showDatePicker(
    activity: AppCompatActivity,
    onDateSelected: (Long?) -> Unit
) {
    val picker = MaterialDatePicker.Builder
        .datePicker()
        .setTheme(R.style.MaterialCalendarTheme)
        .build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener { onDateSelected(it) }
}