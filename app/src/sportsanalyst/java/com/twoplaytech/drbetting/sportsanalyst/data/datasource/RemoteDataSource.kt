package com.twoplaytech.drbetting.sportsanalyst.data.datasource

import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.data.common.Either

/*
    Author: Damjan Miloshevski 
    Created on 3.1.22 17:18
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
interface RemoteDataSource {
    suspend fun getTicketByDate(date: String):Either<Message,Ticket>
}