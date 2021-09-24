package com.twoplaytech.drbetting.domain.usecases

import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.domain.repository.Repository
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 24.9.21 13:13
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class SendFeedbackUseCaseImpl @Inject constructor(repository: Repository) : UseCase(repository),
    SendFeedbackUseCase {
    override fun sendFeedback(
        feedbackMessage: FeedbackMessage,
        onSuccess: (FeedbackMessage) -> Unit,
        onError: (Message) -> Unit
    ) {
        repository.sendFeedback(
            feedbackMessage,
            onSuccess = { onSuccess.invoke(it) },
            onError = { onError.invoke(it) })
    }
}