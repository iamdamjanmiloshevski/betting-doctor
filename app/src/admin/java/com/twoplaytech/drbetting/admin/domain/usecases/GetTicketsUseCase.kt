package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.data.models.Ticket
import kotlinx.coroutines.flow.Flow


/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 13:43
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface GetTicketsUseCase {
    suspend fun getTickets():Flow<List<Ticket>>
    suspend fun getTicketById(id:String):Flow<Ticket>
}