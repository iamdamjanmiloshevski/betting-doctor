package com.twoplaytech.drbetting.sportsanalyst.data.api

import com.twoplaytech.drbetting.data.models.Ticket
import retrofit2.http.GET
import retrofit2.http.Query

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 11:46
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
interface TicketsApi {
    @GET("betting-tickets/search")
    suspend fun getTicketByDate(@Query("date") date: String): Ticket
}