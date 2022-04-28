package com.twoplaytech.drbetting.sportsanalyst.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.sportsanalyst.domain.repository.Repository
import com.twoplaytech.drbetting.sportsanalyst.ui.state.TicketUiState
import com.twoplaytech.drbetting.sportsanalyst.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:23
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@HiltViewModel
class TicketsViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val _ticketUiState:MutableStateFlow<TicketUiState?> = MutableStateFlow(null)
    val ticketUiState:StateFlow<TicketUiState?> = _ticketUiState

    init {
        getTicketByDate(today())
    }

     fun getTicketByDate(date: String) {
        viewModelScope.launch {
            repository.getTicketByDate1(date).onStart {
                _ticketUiState.value = TicketUiState.Loading
            }
                .catch { cause->
                    _ticketUiState.value = TicketUiState.Error(cause)
                }.collect { either ->
                    when(either){
                        is Either.Failure -> {
                            _ticketUiState.value = TicketUiState.Error(Throwable(either.message.message))
                        }
                        is Either.Response -> {
                            val ticket = either.data
                            ticket.let {
                                _ticketUiState.value = TicketUiState.Success(it.tips)
                            }
                        }
                    }
                }
        }
    }
}