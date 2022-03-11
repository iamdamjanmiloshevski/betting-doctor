package com.twoplaytech.drbetting.admin.ui.ticket.widgets

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.google.android.material.datepicker.MaterialDatePicker
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.util.getIconFromTypeFromSelectedItem
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 17.2.22 16:12
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/

fun showDateTimePicker(context: Context, onDateSelected: (Calendar?) -> Unit){
    MaterialDialog(context)
        .cancelable(false)
        .show {
        dateTimePicker(show24HoursView = true) { _, dateTime ->
            // Use dateTime (Calendar)
            onDateSelected.invoke(dateTime)
        }
    }
}
@Preview
@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String> = emptyList(),
    type: DropdownType = DropdownType.SPORT,
    request: (Boolean) -> Unit = {},
    selectedItem: (String,Int) -> Unit = { _, _ ->}
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            val icon = type.getIconFromTypeFromSelectedItem(it)
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedItem(it,icon)
                }
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Spinner icon",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(it, modifier = Modifier.wrapContentWidth(), textAlign = TextAlign.Center)

            }
        }
    }
}

enum class DropdownType {
    SPORT, STATUS
}