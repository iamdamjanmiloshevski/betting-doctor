package com.twoplaytech.drbetting.ui.states

import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.FeedbackMessage

/*
    Author: Damjan Miloshevski 
    Created on 29.4.22 13:48
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
sealed class BettingTipsUiState {
    object Loading : BettingTipsUiState()
    data class Success(val tips: List<BettingTip>) : BettingTipsUiState()
    data class Error(val message: String) : BettingTipsUiState()
}

sealed class FeedbackUiState {
    object Neutral:FeedbackUiState()
    object Loading : FeedbackUiState()
    data class Success(val feedbackMessage: FeedbackMessage) : FeedbackUiState()
    data class Error(val message: String) : FeedbackUiState()
}
