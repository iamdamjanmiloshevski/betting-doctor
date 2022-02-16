package com.twoplaytech.drbetting.admin.ui.ticket.navigation

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 14:56
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
enum class TicketRoute {
    Tickets,
    AddOrUpdateTicket,
    AddBettingTip;

    companion object {
        fun route(route:TicketRoute):String = when(route){
            Tickets -> Tickets.name
            AddOrUpdateTicket -> AddOrUpdateTicket.name
            AddBettingTip -> AddBettingTip.name
        }
    }
}
