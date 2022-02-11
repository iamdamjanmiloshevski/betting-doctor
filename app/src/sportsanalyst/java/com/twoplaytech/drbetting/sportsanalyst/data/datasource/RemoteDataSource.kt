package com.twoplaytech.drbetting.sportsanalyst.data.datasource

import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 3.1.22 17:18
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
interface RemoteDataSource {
    suspend fun getTicketByDate(date:String): Flow<Ticket>
    suspend fun getTicketByDate1(date: String):Ticket
}