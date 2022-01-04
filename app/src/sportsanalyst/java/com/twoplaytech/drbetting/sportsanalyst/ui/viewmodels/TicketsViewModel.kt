package com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.domain.usecases.GetTicketByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getTicketByDate(date: String) {
        getTicketByDateUseCase.getTicketByDate(date, onSuccess = {
            ticketObserver.postValue(Resource.success(null, it))
        }, onError = {
            ticketObserver.postValue(Resource.error(it, null))
        })
    }

    fun observeTicket() = ticketObserver
}