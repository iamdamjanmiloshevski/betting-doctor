package com.twoplaytech.drbetting.admin.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.usecases.GetTicketsUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.InsertTicketUseCase
import com.twoplaytech.drbetting.admin.domain.usecases.UpdateTicketUseCase
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Ticket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val updateTicketUseCase: UpdateTicketUseCase
) :ViewModel() {
    var ticket: Resource<Ticket?> by mutableStateOf(Resource.Success(null))
    var tickets:Resource<List<Ticket>> by mutableStateOf(Resource.Success(emptyList()))
    var bettingTips = mutableStateListOf<BettingTip>()

    init {
        getTickets()
    }

     fun getTickets(){
        viewModelScope.launch {
            tickets = when(val response = getTicketsUseCase.getTickets()){
                is Resource.Error -> {Resource.Error(response.message,null)}
                is Resource.Loading -> {  Resource.Loading(null)}
                is Resource.Success -> {
                    response.data?.let {
                        Resource.Success(it)
                    } ?: throw Exception("List of tickets cannot be null")
                }
            }
        }
    }

    fun getTicketById(id: String) {
        viewModelScope.launch {
            ticket = when(val response = getTicketsUseCase.getTicketById(id)){
                is Resource.Error -> {
                    Resource.Error(response.message,null)
                }
                is Resource.Loading -> {
                    Resource.Loading(null)
                }
                is Resource.Success -> {
                    Resource.Success(response.data)
                }
            }
        }
    }

    fun insertTicket(ticketInput:TicketInput){
        viewModelScope.launch(Dispatchers.IO) { insertTicketUseCase.insertTicket(ticketInput) }
    }

    fun updateTicket(ticketInput: TicketInput) {
        viewModelScope.launch(Dispatchers.IO) { updateTicketUseCase.updateTicket(ticketInput)}
    }
}