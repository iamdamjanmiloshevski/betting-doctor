package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoplaytech.drbetting.sportsanalyst.ui.state.TicketUiState
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SilverChalice
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.sportsanalyst.ui.widgets.showDatePicker
import com.twoplaytech.drbetting.sportsanalyst.util.*
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.ui.common.TipCard
import timber.log.Timber
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 10.2.22 16:19
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun Ticket(
    viewModel: TicketsViewModel
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
fun TicketInfo(viewModel: TicketsViewModel) {
    Surface(modifier = Modifier.fillMaxSize(), color = SilverChalice) {
        when (val state = viewModel.ticketUiState.collectAsState().value) {
            is TicketUiState.Error -> {
                Timber.e(state.exception)
                CenteredItem {
                    Text(
                        text = state.exception.toUserMessage(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            TicketUiState.Loading ->{
                CenteredItem {
                    CircularProgressIndicator()
                }
            }
            is TicketUiState.Success -> {
                LazyColumn(contentPadding = PaddingValues(10.dp)) {
                    items(state.data) { bettingTip ->
                        TipCard(bettingTip = bettingTip)
                    }
                }
            }
            null -> {}
        }
    }
}
@Composable
 fun TicketAppBar(
    activity:AppCompatActivity,
    title:String,
    onDateChanged:(Long) -> Unit = {}
) {
    TopAppBar(title = {
        Text(
            text = title
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

