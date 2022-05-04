package com.twoplaytech.drbetting.admin.ui.common

import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Notification
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
    object Deleted:BettingTipUiState()
    object Neutral:BettingTipUiState()
}
sealed class BettingTipsUiState{
    data class Success(val tips:List<BettingTip>):BettingTipsUiState()
    object Loading:BettingTipsUiState()
    data class Error(val exception:Throwable):BettingTipsUiState()
}
sealed class NotificationUiState{
    data class Success(val notification: Notification):NotificationUiState()
    object Loading:NotificationUiState()
    data class Error(val exception:Throwable):NotificationUiState()
}
sealed class LoginUiState{
    data class Success(val accessToken: AccessToken):LoginUiState()
    object Loading:LoginUiState()
    data class Error(val exception:Throwable):LoginUiState()
    object Neutral:LoginUiState()
}