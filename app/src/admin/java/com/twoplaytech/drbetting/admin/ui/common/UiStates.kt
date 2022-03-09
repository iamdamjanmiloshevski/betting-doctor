package com.twoplaytech.drbetting.admin.ui.common

import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 3.3.22 08:41
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
sealed class TicketsUiState{
    data class Success(val tickets:List<Ticket>):TicketsUiState()
    object Loading:TicketsUiState()
    data class Error(val exception:Throwable):TicketsUiState()
}
sealed class TicketUiState{
    object NewTicket:TicketUiState()
    data class Success(val ticket:Ticket, val modified:Boolean = false):TicketUiState()
    object Loading:TicketUiState()
    data class Error(val exception:Throwable):TicketUiState()
}
sealed class BettingTipUiState{
    data class Success(val bettingTip:BettingTip):BettingTipUiState()
    object Loading:BettingTipUiState()
    data class Error(val exception:Throwable):BettingTipUiState()
}