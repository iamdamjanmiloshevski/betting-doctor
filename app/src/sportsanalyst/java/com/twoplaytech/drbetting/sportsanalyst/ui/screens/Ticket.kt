package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.twoplaytech.drbetting.sportsanalyst.ui.components.TicketInfo
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.sportsanalyst.ui.widgets.showDatePicker
import com.twoplaytech.drbetting.sportsanalyst.util.*
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 10.2.22 16:19
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun Ticket(
    navController: NavController = NavController(LocalContext.current),
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val date = Date(System.currentTimeMillis())
    val activity = LocalContext.current as AppCompatActivity
    val titleState = remember {
        mutableStateOf(date.format())
    }
    val dateState = remember {
        mutableStateOf(today())
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TicketAppBar(activity, title = titleState.value){
            titleState.value = it.toZonedDate().beautify()
            dateState.value = it.toServerDate()
            viewModel.getTicketByDate(dateState.value)
        }
    }) {
        TicketInfo(viewModel)
    }
}

@Composable
private fun TicketAppBar(
    activity:AppCompatActivity,
    title:String,
    onDateChanged:(Long) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = "Ticket $title"
        )
    }, actions = {
        Image(
            imageVector = Icons.Outlined.DateRange,
            contentDescription = "Date range",
            modifier = Modifier.clickable {
                showDatePicker(activity) { newDate ->
                    newDate?.let {onDateChanged.invoke(newDate)}
                }
            }
        )
    }, backgroundColor = SeaGreen)
}

