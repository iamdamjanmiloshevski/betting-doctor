package com.twoplaytech.drbetting.admin.ui.ticket.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.data.mappers.TicketMapper
import com.twoplaytech.drbetting.admin.ui.common.NotificationUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketFloatingActionButton
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.navigation.TicketRoute
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.admin.util.beautify
import com.twoplaytech.drbetting.admin.util.update
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.ui.common.TipCard
import com.twoplaytech.drbetting.util.toStringDate
import com.twoplaytech.drbetting.util.toast
import com.twoplaytech.drbetting.util.today
import timber.log.Timber
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
    val enableNewTips = remember {
        mutableStateOf(false)
    }
    val bettingTipsState = remember {
        ticketsViewModel.bettingTips
    }
    val enableNotifications =  remember {
        mutableStateOf(false)
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
                        it.tips.update(bettingTipsState)
                        isVisible.value = false
                        ticketsViewModel.updateTicket(TicketMapper.toTicketInput(it))
                    } ?: throw NullPointerException("Ticket must not be null!")
                } else {
                    val newTicket =
                        Ticket(
                            date = Calendar.getInstance().time.toStringDate(),
                            tips = bettingTipsState
                        )
                    isVisible.value = false
                    enableNotifications.value = true
                    ticketsViewModel.insertTicket(TicketMapper.toTicketInput(newTicket))
                }
            }
            MenuAction(
                icon = Icons.Default.Notifications,
                contentDescription = "Send notification",
                isVisible = enableNotifications
            ) {
                ticketsViewModel.sendNotification("Ticket")
            }
        })
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = R.color.silver_chalice)
        ) {
            val context = LocalContext.current
            ticket = observeTicketState(
                ticketsViewModel,
                enableNewTips,
                bettingTipsState,
                isVisible,
                navController,
                ticket,
                ticketTitle
            )
            if (enableNewTips.value) {
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
                                    TicketRoute.AddOrUpdateBettingTip
                                ).plus("?ticketId=${it.id},?tipId=${null}")
                            )
                        }
                    } else {
                        navController.navigate(
                            TicketRoute.route(
                                TicketRoute.AddOrUpdateBettingTip
                            )
                        )
                    }
                }
            }
            ObserveNotifications(ticketsViewModel, context)
        }
    }
}

@Composable
private fun ObserveNotifications(
    ticketsViewModel: TicketsViewModel,
    context: Context
) {
    when (val notificationState = ticketsViewModel.notificationState.collectAsState().value) {
        is NotificationUiState.Error -> {
            context.toast(
                notificationState.exception.message ?: "Error while trying to send notification",
                Toast.LENGTH_SHORT
            )
        }
        NotificationUiState.Loading -> {
            Timber.i("Sending notification...")
        }
        is NotificationUiState.Success -> {
            Timber.i("Notification ${notificationState.notification} sent successfully!")
        }
        null -> {}
    }
}

@Composable
private fun observeTicketState(
    ticketsViewModel: TicketsViewModel,
    enableNewTips: MutableState<Boolean>,
    bettingTipsState: SnapshotStateList<BettingTip>,
    isVisible: MutableState<Boolean>,
    navController: NavController,
    ticket: Ticket?,
    ticketTitle: MutableState<String>
): Ticket? {
    var ticket = ticket
    when (val state = ticketsViewModel.ticketUiState.collectAsState().value) {
        is TicketUiState.Error -> {

        }
        TicketUiState.Loading -> {
            CenteredItem { CircularProgressIndicator() }
        }
        TicketUiState.NewTicket -> {
            enableNewTips.value = true
            if (bettingTipsState.isNotEmpty()) {
                isVisible.value = true
                ShowBettingTips(navController, bettingTipsState)
            } else {
                CenteredItem {
                    Text(text = "No tips for this ticket")
                }
            }
        }
        is TicketUiState.Success -> {
            ticket = state.ticket
            TicketInfo(
                navController,
                isVisible,
                ticket,
                bettingTipsState,
                ticketsViewModel,
                ticketTitle
            )
            if (state.modified) {
                enableNewTips.value = false
                bettingTipsState.clear()
                navController.navigateUp()
            }
        }
        else -> throw Exception("I don't know what state I'm in")
    }
    return ticket
}


@Composable
private fun TicketInfo(
    navController: NavController,
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
                tips.forEachIndexed { index, bettingTip ->
                    val iTip = ticketsViewModel.bettingTips[index]
                    if (bettingTip._id == iTip._id) {
                        iTip.copy(
                            leagueName = bettingTip.leagueName,
                            teamHome = bettingTip.teamHome,
                            teamAway = bettingTip.teamAway,
                            gameTime = bettingTip.gameTime,
                            bettingType = bettingTip.bettingType,
                            status = bettingTip.status,
                            result = bettingTip.result,
                            sport = bettingTip.sport,
                            coefficient = bettingTip.coefficient,
                            ticketId = bettingTip.ticketId
                        )
                    } else ticketsViewModel.bettingTips.add(bettingTip)
                }
                isVisible.value = tips != ticketsViewModel.bettingTips
            } else ticketsViewModel.bettingTips.addAll(tips)
            ShowBettingTips(navController, bettingTipsState, this.id)
        }
    } else {
        ShowBettingTips(navController, bettingTipsState)
    }
}

@Composable
private fun ShowBettingTips(
    navController: NavController,
    bettingTips: SnapshotStateList<BettingTip>,
    ticketId: String? = null
) {
    if (bettingTips.isNullOrEmpty()) {
        CenteredItem {
            Text(text = "No tips for this ticket. Please add some")
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(10.dp)) {
            items(bettingTips) { bettingTip: BettingTip ->
                TipCard(bettingTip = bettingTip) {
                    navController.navigate(
                        TicketRoute.route(TicketRoute.AddOrUpdateBettingTip)
                            .plus("?ticketId=${ticketId},?tipId=${it._id}")
                    )
                }
            }
        }
    }
}