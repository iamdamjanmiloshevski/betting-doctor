package com.twoplaytech.drbetting.domain.usecases

import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Message

/*
    Author: Damjan Miloshevski 
    Created on 24.9.21 13:12
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface SendFeedbackUseCase {
    fun sendFeedback(
        feedbackMessage: FeedbackMessage,
        onSuccess: (FeedbackMessage) -> Unit,
        onError: (Message) -> Unit
    )
}