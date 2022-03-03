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
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.data.mappers.TicketMapper
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketFloatingActionButton
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.navigation.TicketRoute
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.admin.util.beautify
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.ui.common.TipCard
import com.twoplaytech.drbetting.util.toStringDate
import com.twoplaytech.drbetting.util.today
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:04
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/

@Composable
fun AddOrUpdateTicket(
    navController: NavController = NavController(LocalContext.current),
    ticketsViewModel: TicketsViewModel
) {
    val ticketTitle = remember {
        mutableStateOf(today().beautify())
    }
    var ticket: Ticket? = null
    val isVisible = remember {
        mutableStateOf(false)
    }
    val bettingTipsState = remember {
        ticketsViewModel.bettingTips
    }
    Scaffold(topBar = {
        TicketsAppBar(title = ticketTitle.value, navController = navController, actions = {
            MenuAction(
                icon = Icons.Default.Check,
                contentDescription = "Save icon",
                isVisible
            ) {
                if (ticket != null) {
                    ticket?.let {
                        it.tips = bettingTipsState
                        ticketsViewModel.updateTicket(TicketMapper.toTicketInput(it))
                    } ?: throw NullPointerException("Ticket must not be null!")
                } else {
                    val newTicket =
                        Ticket(
                            date = Calendar.getInstance().time.toStringDate(),
                            tips = bettingTipsState
                        )
                    ticketsViewModel.insertTicket(TicketMapper.toTicketInput(newTicket))
                }
                navController.navigateUp()
            }
        })
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.silver_chalice)
        ) {
            when (val state = ticketsViewModel.ticketUiState.collectAsState().value) {
                is TicketUiState.Error -> {

                }
                TicketUiState.Loading -> {
                    CenteredItem { CircularProgressIndicator() }
                }
                TicketUiState.NewTicket -> {
                    CenteredItem {
                        Text(text = "No tips for this ticket. Please add some")
                    }
                }
                is TicketUiState.Success -> {
                    ticket = state.ticket
                    TicketInfo(isVisible,ticket,bettingTipsState, ticketsViewModel, ticketTitle)
                }
                else -> throw Exception("I don't know what state I'm in")
            }

            TicketFloatingActionButton(
                modifier = Modifier.padding(
                    end = 10.dp,
                    bottom = 10.dp
                ),
                icon = Icons.Default.Add,
                contentDescription = "Add Betting Tip Icon"
            ) {
                if (ticket != null) {
                    ticket?.let {
                        navController.navigate(
                            TicketRoute.route(
                                TicketRoute.AddBettingTip
                            ).plus("?ticketId=${it.id}")
                        )
                    }
                } else {
                    navController.navigate(
                        TicketRoute.route(
                            TicketRoute.AddBettingTip
                        )
                    )
                }
            }
        }
    }
}


@Composable
private fun TicketInfo(
    isVisible: MutableState<Boolean>,
    ticket: Ticket?,
    bettingTipsState: SnapshotStateList<BettingTip>,
    ticketsViewModel: TicketsViewModel,
    ticketTitle: MutableState<String>
) {
    if (ticket != null) {
        ticketTitle.value = ticket.date
        ticketsViewModel.initialList = ticket.tips
        isVisible.value = ticketsViewModel.initialList.size < ticketsViewModel.bettingTips.size
        with(ticket) {
            if (bettingTipsState.isNotEmpty()) {
                tips.forEach {
                    if (!ticketsViewModel.bettingTips.contains(it)) ticketsViewModel.bettingTips.add(
                        it
                    )
                }
            } else ticketsViewModel.bettingTips.addAll(tips)
            ShowBettingTips(bettingTipsState)
        }
    } else {
        ShowBettingTips(bettingTipsState)
    }
}

@Composable
private fun ShowBettingTips(bettingTips: SnapshotStateList<BettingTip>) {
    if (bettingTips.isNullOrEmpty()) {
        CenteredItem {
            Text(text = "No tips for this ticket. Please add some")
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(10.dp)) {
            items(bettingTips) { bettingTip: BettingTip ->
                TipCard(bettingTip = bettingTip) {}
            }
        }
    }
}