package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketsUiState
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketCard
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketFloatingActionButton
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.navigation.TicketRoute
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.ui.common.CenteredItem

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:03
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun Tickets(
    activity: AppCompatActivity = LocalContext.current as AppCompatActivity,
    navController: NavController = NavController(LocalContext.current),
    ticketsViewModel: TicketsViewModel
) {
    ticketsViewModel.getTickets()
    Scaffold(topBar = {
        TicketsAppBar(title = "Tickets", activity = activity)
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.silver_chalice)
        ) {
            when (val state = ticketsViewModel.ticketsUiState.collectAsState().value) {
                is TicketsUiState.Error -> {
                    val message = state.exception.localizedMessage
                    CenteredItem {
                        Text(text = message)
                    }
                }
                TicketsUiState.Loading -> {
                    CenteredItem {
                        CircularProgressIndicator()
                    }
                }
                is TicketsUiState.Success -> {
                    val tickets = state.tickets
                    LazyColumn(contentPadding = PaddingValues(10.dp)) {
                        items(tickets) { ticket ->
                            TicketCard(ticket = ticket, onLongPress = {
                                ticketsViewModel.deleteTicket(it)
                            }) { ticketSelected ->
                                ticketSelected.id?.let {
                                    ticketsViewModel.getTicketById(it)
                                    navController.navigate(
                                        TicketRoute.route(TicketRoute.AddOrUpdateTicket)
                                    )
                                }
                            }
                        }
                    }
                }
                else -> throw Exception("I don't know what state I'm in")
            }
            TicketFloatingActionButton(
                modifier = Modifier.padding(
                    end = 10.dp,
                    bottom = 10.dp
                ),
                icon = Icons.Default.Add,
                contentDescription = "Add Ticket Icon"
            ) {
                ticketsViewModel.setTicketState(TicketUiState.NewTicket)
                navController.navigate(
                    TicketRoute.route(
                        TicketRoute.AddOrUpdateTicket
                    )
                )
            }
        }
    }
}




