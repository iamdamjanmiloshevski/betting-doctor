package com.twoplaytech.drbetting.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.usecases.SendFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 24.9.21 13:10
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class FeedbackViewModel @Inject constructor(private val sendFeedbackUseCase: SendFeedbackUseCase) :
    ViewModel() {
    private val nameErrorObserver = ObservableBoolean()
    private val emailErrorObserver = ObservableBoolean()
    private val messageErrorObserver = ObservableBoolean()
    private val feedbackObserver = MutableLiveData<Resource<FeedbackMessage>>()

    fun setNameError(hasError: Boolean) {
        nameErrorObserver.set(hasError)
    }

    fun setEmailError(hasError: Boolean) {
        emailErrorObserver.set(hasError)
    }

    fun setMessageError(hasError: Boolean) {
        messageErrorObserver.set(hasError)
    }


    fun sendFeedback(feedbackMessage: FeedbackMessage) {
        feedbackObserver.postValue(Resource.loading("Sending feedback\nPlease wait...",null))
        sendFeedbackUseCase.sendFeedback(feedbackMessage, onSuccess = {
            feedbackObserver.postValue(Resource.success("Success", it))
        }, onError = {
            feedbackObserver.postValue(Resource.error(it.message, null))
        })
    }

    fun emailError() = emailErrorObserver.get()
    fun nameError() = nameErrorObserver.get()
    fun messageError() = messageErrorObserver.get()
    fun observeFeedback() = feedbackObserver
}