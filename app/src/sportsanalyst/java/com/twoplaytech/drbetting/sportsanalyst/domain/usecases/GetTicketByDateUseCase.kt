package com.twoplaytech.drbetting.sportsanalyst.domain.usecases

import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:12
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
interface GetTicketByDateUseCase {
    fun getTicketByDate(date:String, onSuccess:(Ticket) ->Unit, onError:(Message)->Unit)
}