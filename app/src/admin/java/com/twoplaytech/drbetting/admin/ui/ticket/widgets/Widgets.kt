package com.twoplaytech.drbetting.admin.ui.ticket.widgets

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.android.material.datepicker.MaterialDatePicker
import com.twoplaytech.drbetting.R

/*
    Author: Damjan Miloshevski 
    Created on 17.2.22 16:12
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
fun showDatePicker(
    activity : AppCompatActivity,
    onDateSelected: (Long?) -> Unit)
{
    val picker = MaterialDatePicker.Builder
        .datePicker()
        .setTheme(R.style.MaterialCalendarTheme)
        .build()
    picker.show(activity.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener { onDateSelected(it) }
}
@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(it, modifier = Modifier.wrapContentWidth(), textAlign = TextAlign.Center)
            }
        }
    }
}