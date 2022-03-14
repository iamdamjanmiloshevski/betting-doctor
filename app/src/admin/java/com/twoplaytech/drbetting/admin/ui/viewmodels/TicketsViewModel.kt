package com.twoplaytech.drbetting.admin.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.usecases.*
import com.twoplaytech.drbetting.admin.ui.common.BettingTipUiState
import com.twoplaytech.drbetting.admin.ui.common.NotificationUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketsUiState
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
class TicketsViewModel @Inject constructor(
    private val getTicketsUseCase: GetTicketsUseCase,
    private val insertTicketUseCase: InsertTicketUseCase,
    private val updateTicketUseCase: UpdateTicketUseCase,
    private val deleteTicketUseCase: DeleteTicketUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {
    var ticket: Resource<Ticket?> by mutableStateOf(Resource.Success(null))
    private val _ticketsUiState: MutableStateFlow<TicketsUiState?> = MutableStateFlow(null)
    val ticketsUiState: StateFlow<TicketsUiState?> = _ticketsUiState
    private val _ticketUiState: MutableStateFlow<TicketUiState?> = MutableStateFlow(null)
    val ticketUiState: StateFlow<TicketUiState?> = _ticketUiState
    private val _bettingTipUiState: MutableStateFlow<BettingTipUiState?> = MutableStateFlow(null)
    val bettingTipUiState: StateFlow<BettingTipUiState?> = _bettingTipUiState
    private val _notificationState: MutableStateFlow<NotificationUiState?> =
        MutableStateFlow(null)
    val notificationState:StateFlow<NotificationUiState?> = _notificationState
    val bettingTips = mutableStateListOf<BettingTip>()
    var initialList = listOf<BettingTip>()

    init {
        getTickets()
    }

    fun getTickets() {
        bettingTips.clear()
        viewModelScope.launch {
            getTicketsUseCase.getTickets()
                .catch { cause ->
                    _ticketsUiState.value = TicketsUiState.Error(cause)
                }
                .onStart { _ticketsUiState.value = TicketsUiState.Loading }
                .onCompletion { cause -> cause?.let { TicketsUiState.Error(it) } }
                .collect {
                    if (it.isEmpty()) {
                        _ticketsUiState.value =
                            TicketsUiState.Error(Throwable("No tickets available"))
                    } else _ticketsUiState.value = TicketsUiState.Success(it)
                }

        }
    }

    fun setTicketState(ticketUiState: TicketUiState) {
        _ticketUiState.value = ticketUiState
    }

    fun getTicketById(id: String) {
        viewModelScope.launch {
            getTicketsUseCase.getTicketById(id)
                .catch { cause -> _ticketUiState.value = TicketUiState.Error(cause) }
                .onStart { _ticketUiState.value = TicketUiState.Loading }
                .onCompletion { cause -> cause?.let { TicketUiState.Error(it) } }
                .collect {
                    initialList = it.tips
                    _ticketUiState.value = TicketUiState.Success(it)
                }
        }
    }


    fun insertTicket(ticketInput: TicketInput) {
        viewModelScope.launch(Dispatchers.IO) {
            insertTicketUseCase.insertTicket(ticketInput).catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.onCompletion { cause ->
                cause?.let { _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                _ticketUiState.value = TicketUiState.Success(it, true)
            }

        }
    }

    fun updateTicket(ticketInput: TicketInput) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTicketUseCase.updateTicket(ticketInput).catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.onCompletion { cause ->
                cause?.let { _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                _ticketUiState.value = TicketUiState.Success(it, true)
            }
        }
    }

    fun deleteTicket(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTicketUseCase.deleteTicket(id).catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.onCompletion { cause ->
                cause?.let { _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                Timber.e(it.message)
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
        viewModelScope.launch(Dispatchers.IO) {
            sendNotificationUseCase.sendNotification(Notification(topic))
                .onStart {
                    Timber.i("Sending notification to topic $topic")
                    _notificationState.value = NotificationUiState.Loading
                }.catch { e ->
                    _notificationState.value = NotificationUiState.Error(e)
                }.collect {
                    _notificationState.value = NotificationUiState.Success(it)
                }
        }
    }
}