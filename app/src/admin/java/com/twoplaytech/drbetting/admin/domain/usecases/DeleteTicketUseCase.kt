package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.data.models.Message
import kotlinx.coroutines.flow.Flow


/*
    Author: Damjan Miloshevski 
    Created on 3.3.22 13:34
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface DeleteTicketUseCase {
    suspend fun deleteTicket(id:String): Flow<Message>
}