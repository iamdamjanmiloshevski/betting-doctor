package com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.domain.usecases.GetTicketByDateUseCase
import com.twoplaytech.drbetting.sportsanalyst.util.today
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:23
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@HiltViewModel
class TicketsViewModel @Inject constructor(private val getTicketByDateUseCase: GetTicketByDateUseCase) :
    ViewModel() {
    var ticket: Resource<Ticket?> by mutableStateOf(Resource.Success(null))

    init {
        getTicketByDate(today())
    }

     fun getTicketByDate(date: String) {
        viewModelScope.launch {
            ticket = when(val response = getTicketByDateUseCase.getTicketByDate(date)){
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
}