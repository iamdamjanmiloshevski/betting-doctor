package com.twoplaytech.drbetting.admin.data.mappers

import androidx.annotation.Keep
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 23.2.22 15:50
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Keep
object TicketMapper {
    fun toTicketInput(ticket: Ticket) =
        TicketInput(
            ticket.date,
            ticket.id,
            ticket.tips.map { BettingTipMapper.toBettingTipInput(it) })
}