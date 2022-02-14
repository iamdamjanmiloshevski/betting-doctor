package com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
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
    private val ticketObserver = MutableLiveData<Resource<Ticket>>()
    suspend fun getTicketByDate1(date: String): com.twoplaytech.drbetting.sportsanalyst.data.Resource<Ticket> =
        getTicketByDateUseCase.getTicketByDate1(date)
    var ticket:com.twoplaytech.drbetting.sportsanalyst.data.Resource<Ticket?> by mutableStateOf(com.twoplaytech.drbetting.sportsanalyst.data.Resource.Success(null))

    init {
        getTicketByDate2(today())
    }

     fun getTicketByDate2(today: String) {
        viewModelScope.launch {
            ticket = when(val response = getTicketByDateUseCase.getTicketByDate1(today)){
                is com.twoplaytech.drbetting.sportsanalyst.data.Resource.Error -> {
                    com.twoplaytech.drbetting.sportsanalyst.data.Resource.Error(response.message,null)
                }
                is com.twoplaytech.drbetting.sportsanalyst.data.Resource.Loading -> {
                    com.twoplaytech.drbetting.sportsanalyst.data.Resource.Loading(null)
                }
                is com.twoplaytech.drbetting.sportsanalyst.data.Resource.Success -> {
                    com.twoplaytech.drbetting.sportsanalyst.data.Resource.Success(response.data)
                }
            }
        }
    }

    fun getTicketByDate(date: String) {
        getTicketByDateUseCase.getTicketByDate(date, onSuccess = {
            ticketObserver.postValue(Resource.success(null, it))
        }, onError = {
            ticketObserver.postValue(Resource.error(MessageMapper.toJson(it), null))
        })
    }

    fun observeTicket() = ticketObserver
}