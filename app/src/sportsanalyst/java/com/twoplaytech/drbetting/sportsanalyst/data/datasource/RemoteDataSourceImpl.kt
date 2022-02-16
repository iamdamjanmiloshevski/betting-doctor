package com.twoplaytech.drbetting.sportsanalyst.data.datasource

import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.data.api.TicketsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 12:04
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
class RemoteDataSourceImpl @Inject constructor(private val api: TicketsApi) : RemoteDataSource,
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override suspend fun getTicketByDate(date: String): Ticket { return api.getTicketByDate(date)}
}