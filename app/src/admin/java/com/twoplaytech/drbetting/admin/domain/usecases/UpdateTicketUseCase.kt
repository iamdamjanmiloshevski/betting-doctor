package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.data.models.Ticket
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 23.2.22 15:24
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface UpdateTicketUseCase {
    suspend fun updateTicket( ticket: TicketInput): Flow<Ticket>
}