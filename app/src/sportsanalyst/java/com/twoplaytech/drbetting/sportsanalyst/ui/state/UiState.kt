package com.twoplaytech.drbetting.sportsanalyst.ui.state

import com.twoplaytech.drbetting.data.models.BettingTip

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 08:43
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
sealed class TicketUiState{
    object Loading: TicketUiState()
    data class Success(val data:List<BettingTip>):TicketUiState()
    data class Error(val exception:Throwable):TicketUiState()
}