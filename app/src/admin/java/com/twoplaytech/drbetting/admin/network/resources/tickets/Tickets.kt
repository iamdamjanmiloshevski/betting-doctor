package com.twoplaytech.drbetting.admin.network.resources.tickets

import io.ktor.resources.*
import kotlinx.serialization.Serializable

/*
    Author: Damjan Miloshevski 
    Created on 5.5.22 16:29
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Serializable
@Resource("betting-tickets")
class Tickets(val date: String? = null) {
    @Serializable
    @Resource("{id}")
    class Id(val parent: Tickets = Tickets(), val id: String)
}