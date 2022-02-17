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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.data.Resource
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
@Preview
@Composable
fun Tickets(
    activity: AppCompatActivity = LocalContext.current as AppCompatActivity,
    navController: NavController = NavController(LocalContext.current),
    ticketsViewModel: TicketsViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        TicketsAppBar(title = "Tickets", activity = activity)
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.silver_chalice)
        ) {
            when (val tickets = ticketsViewModel.tickets) {
                is Resource.Error -> {
                    CenteredItem {
                        Text(text = tickets.message.toString())
                    }
                }
                is Resource.Loading -> {
                    CenteredItem {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    tickets.data?.let {
                        LazyColumn(contentPadding = PaddingValues(10.dp)) {
                            items(it) { ticket ->
                                TicketCard(ticket = ticket) { id ->
                                    ticketsViewModel.getTicketById(id)
                                    navController.navigate(
                                        TicketRoute.route(TicketRoute.AddOrUpdateTicket)
                                            .plus("?ticketId=${id}")
                                    )
                                }
                            }
                        }
                    }
                }
            }
            TicketFloatingActionButton(
                modifier = Modifier.padding(
                    end = 10.dp,
                    bottom = 10.dp
                ),
                icon = Icons.Default.Add,
                contentDescription = "Add Ticket Icon"
            ){
                navController.navigate(
                    TicketRoute.route(
                        TicketRoute.AddOrUpdateTicket
                    )
                )
            }
        }
    }
}




