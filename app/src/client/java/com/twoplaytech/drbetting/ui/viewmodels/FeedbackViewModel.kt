package com.twoplaytech.drbetting.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.domain.common.Resource
import com.twoplaytech.drbetting.domain.repository.Repository
import com.twoplaytech.drbetting.domain.usecases.SendFeedbackUseCase
import com.twoplaytech.drbetting.ui.states.FeedbackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 24.9.21 13:10
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val repository: Repository
) :
    ViewModel() {
    private val nameErrorObserver = ObservableBoolean()
    private val emailErrorObserver = ObservableBoolean()
    private val messageErrorObserver = ObservableBoolean()
    private val feedbackObserver = MutableLiveData<Resource<FeedbackMessage>>()
    private val _feedbackUiState:MutableStateFlow<FeedbackUiState> = MutableStateFlow(FeedbackUiState.Neutral)
    val feedbackState = _feedbackUiState

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
        viewModelScope.launch {
            repository.sendFeedback(feedbackMessage).onStart {
                _feedbackUiState.value = FeedbackUiState.Loading
            }.catch {e ->
            _feedbackUiState.value = FeedbackUiState.Error(e.message ?: "Something went wrong")
            }.collectLatest { either ->
                when(either){
                    is Either.Failure -> _feedbackUiState.value = FeedbackUiState.Error(either.message.message)
                    is Either.Response -> _feedbackUiState.value = FeedbackUiState.Success(either.data)
                }
            }
        }
//        feedbackObserver.postValue(Resource.loading("Sending feedback\nPlease wait...", null))
//        sendFeedbackUseCase.sendFeedback(feedbackMessage, onSuccess = {
//            feedbackObserver.postValue(Resource.success("Success", it))
//        }, onError = {
//            feedbackObserver.postValue(Resource.error(it.message, null))
//        })
    }

    fun emailError() = emailErrorObserver.get()
    fun nameError() = nameErrorObserver.get()
    fun messageError() = messageErrorObserver.get()
    fun observeFeedback() = feedbackObserver
}