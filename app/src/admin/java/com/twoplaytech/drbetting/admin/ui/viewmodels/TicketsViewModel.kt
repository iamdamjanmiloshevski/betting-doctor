package com.twoplaytech.drbetting.admin.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.admin.domain.usecases.*
import com.twoplaytech.drbetting.admin.ui.common.BettingTipUiState
import com.twoplaytech.drbetting.admin.ui.common.NotificationUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketsUiState
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Notification
import com.twoplaytech.drbetting.data.models.Ticket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 12:35
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@HiltViewModel
class TicketsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    var ticket: Resource<Ticket?> by mutableStateOf(Resource.Success(null))
    private val _ticketsUiState: MutableStateFlow<TicketsUiState?> = MutableStateFlow(null)
    val ticketsUiState: StateFlow<TicketsUiState?> = _ticketsUiState
    private val _ticketUiState: MutableStateFlow<TicketUiState?> = MutableStateFlow(null)
    val ticketUiState: StateFlow<TicketUiState?> = _ticketUiState
    private val _bettingTipUiState: MutableStateFlow<BettingTipUiState?> = MutableStateFlow(null)
    val bettingTipUiState: StateFlow<BettingTipUiState?> = _bettingTipUiState
    private val _notificationState: MutableStateFlow<NotificationUiState?> =
        MutableStateFlow(null)
    val notificationState: StateFlow<NotificationUiState?> = _notificationState
    val bettingTips = mutableStateListOf<BettingTip>()
    var initialList = listOf<BettingTip>()

    init {
        getTickets()
    }

    fun getTickets() {
        bettingTips.clear()
        viewModelScope.launch {
            repository.getTickets().onStart {
                _ticketsUiState.value = TicketsUiState.Loading
            }
                .catch { cause ->
                    _ticketsUiState.value = TicketsUiState.Error(cause)
                }
                .collectLatest { either ->
                    when (either) {
                        is Either.Failure -> _ticketsUiState.value =
                            TicketsUiState.Error(Throwable(either.message.message))
                        is Either.Response -> {
                            val tickets = either.data
                            if (tickets.isEmpty()) {
                                _ticketsUiState.value =
                                    TicketsUiState.Error(Throwable("No tickets available"))
                            } else _ticketsUiState.value = TicketsUiState.Success(tickets)
                        }
                    }
                }
        }
    }

    fun setTicketState(ticketUiState: TicketUiState) {
        _ticketUiState.value = ticketUiState
    }

    fun getTicketById(id: String) {
        viewModelScope.launch {
            repository.getTicketById(id).onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.collectLatest { either ->
                when(either){
                    is Either.Failure ->   _ticketUiState.value = TicketUiState.Error(Throwable(either.message.message))
                    is Either.Response -> {
                        val ticket = either.data
                        initialList = ticket.tips
                        _ticketUiState.value = TicketUiState.Success(ticket)
                    }
                }
            }
        }
    }


    fun insertTicket(ticketInput: TicketInput) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTicket(ticketInput).onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.collectLatest { either ->
                when(either){
                    is Either.Failure -> _ticketUiState.value = TicketUiState.Error(Throwable(either.message.message))
                    is Either.Response ->  _ticketUiState.value = TicketUiState.Success(either.data, true)
                }
            }
        }
    }

    fun updateTicket(ticketInput: TicketInput) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTicket(ticketInput).onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.collectLatest { either ->
                when(either){
                    is Either.Failure -> _ticketUiState.value = TicketUiState.Error(Throwable(either.message.message))
                    is Either.Response ->  _ticketUiState.value = TicketUiState.Success(either.data, true)
                }
            }
        }
    }

    fun deleteTicket(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTicketById(id).onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.collectLatest { either ->
                when(either){
                    is Either.Failure -> TicketUiState.Error(Throwable(either.message.message))
                    is Either.Response -> Timber.i(either.data.message)
                }
            }
        }
    }

    fun getBettingTipById(id: String) {
        _bettingTipUiState.value = BettingTipUiState.Loading
        viewModelScope.launch {
            if (bettingTips.isEmpty()) {
                _bettingTipUiState.value = BettingTipUiState.Error(Exception("No items"))
            } else {
                val filtered = bettingTips.filter { bettingTip -> bettingTip._id == id }
                if (filtered.isNotEmpty()) {
                    _bettingTipUiState.value = BettingTipUiState.Success(filtered.first())
                } else _bettingTipUiState.value = BettingTipUiState.Error(Exception("No items"))
            }
        }
    }

    fun updateBettingTip(bettingTip: BettingTip) {
        val iterator = bettingTips.listIterator()
        while (iterator.hasNext()) {
            val bTip = iterator.next()
            if (bTip._id == bettingTip._id) {
                iterator.set(bettingTip)
            }
        }
    }

    fun sendNotification(topic: String) {
        viewModelScope.launch {
            repository.sendNotification(Notification(topic))
                .catch { e ->
                    _notificationState.value = NotificationUiState.Error(e)
                }
                .collectLatest { either ->
                    when (either) {
                        is Either.Failure -> _notificationState.value =
                            NotificationUiState.Error(Throwable(either.message.message))
                        is Either.Response -> _notificationState.value =
                            NotificationUiState.Success(either.data)
                    }
                }
        }
    }
}