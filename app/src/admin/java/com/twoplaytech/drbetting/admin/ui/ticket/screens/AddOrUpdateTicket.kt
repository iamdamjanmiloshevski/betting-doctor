package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketFloatingActionButton
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.navigation.TicketRoute
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.admin.util.beautify
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.ui.common.TipCard
import com.twoplaytech.drbetting.util.today

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:04
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun AddOrUpdateTicket(
    navController: NavController = NavController(LocalContext.current),
    ticketId: String? = null,
    ticketsViewModel: TicketsViewModel = hiltViewModel()
){
    val ticketTitle = remember {
        mutableStateOf(today().beautify())
    }
    Scaffold(topBar = {
        TicketsAppBar(title = ticketTitle.value, navController = navController, actions = {
            MenuAction(
            icon = Icons.Default.Check,
            contentDescription = "Save icon"
        ) { //todo save ticket
        }
        })
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.silver_chalice)
        ){
            TicketInfo(ticketId, ticketsViewModel, ticketTitle)
            TicketFloatingActionButton(
                modifier = Modifier.padding(
                    end = 10.dp,
                    bottom = 10.dp
                ),
                icon = Icons.Default.Add,
                contentDescription = "Add Betting Tip Icon"
            ){
                navController.navigate(
                    TicketRoute.route(
                        TicketRoute.AddBettingTip
                    )
                )
            }
        }
    }
}

@Composable
private fun TicketInfo(
    ticketId: String?,
    ticketsViewModel: TicketsViewModel,
    ticketTitle: MutableState<String>
) {
    if (!ticketId.isNullOrEmpty()) {
        when (val ticketResponse = ticketsViewModel.ticket) {
            is Resource.Error -> {
                CenteredItem {
                    Text(text = ticketResponse.message!!)
                }
            }
            is Resource.Loading -> {
                CenteredItem {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val ticket = ticketResponse.data
                if (ticket != null) {
                    ticketTitle.value = ticket.date
                    with(ticket) {
                        if (this.tips.isNullOrEmpty()) {
                            CenteredItem {
                                Text(text = "No tips for this ticket. Please add some")
                            }
                        } else {
                            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                                items(this@with.tips) { bettingTip: BettingTip ->
                                    TipCard(bettingTip = bettingTip) {
                                        //todo delete dialog
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        ticketTitle.value = today().beautify()
        CenteredItem {
            Text(text = "No tips for this ticket. Please add some")
        }
    }
}