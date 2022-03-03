package com.twoplaytech.drbetting.admin.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.usecases.DeleteTicketUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.GetTicketsUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.InsertTicketUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.UpdateTicketUseCase
import com.twoplaytech.drbetting.admin.ui.common.TicketUiState
import com.twoplaytech.drbetting.admin.ui.common.TicketsUiState
import com.twoplaytech.drbetting.data.models.BettingTip
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
    private val deleteTicketUseCase: DeleteTicketUseCase
) : ViewModel() {
    var ticket: Resource<Ticket?> by mutableStateOf(Resource.Success(null))
    private val _ticketsUiState: MutableStateFlow<TicketsUiState?> = MutableStateFlow(null)
    val ticketsUiState: StateFlow<TicketsUiState?> = _ticketsUiState
    private val _ticketUiState: MutableStateFlow<TicketUiState?> = MutableStateFlow(null)
    val ticketUiState: StateFlow<TicketUiState?> = _ticketUiState
    val bettingTips = mutableStateListOf<BettingTip>()
    var initialList = listOf<BettingTip>()

    init {
        getTickets()
    }

    fun getTickets() {
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
               cause?.let {  _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                _ticketUiState.value = TicketUiState.Success(it)
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
                cause?.let {  _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                _ticketUiState.value = TicketUiState.Success(it)
            }
        }
    }
    fun deleteTicket(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            deleteTicketUseCase.deleteTicket(id).catch { cause ->
                _ticketUiState.value = TicketUiState.Error(cause)
            }.onStart {
                _ticketUiState.value = TicketUiState.Loading
            }.onCompletion { cause ->
                cause?.let {  _ticketUiState.value = TicketUiState.Error(cause) }
            }.collect {
                Timber.e(it.message)
            }
        }
    }
}