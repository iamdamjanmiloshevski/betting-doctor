package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.data.models.Ticket
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 22.2.22 15:56
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface InsertTicketUseCase {
    suspend fun insertTicket(ticket: TicketInput): Flow<Ticket>
}