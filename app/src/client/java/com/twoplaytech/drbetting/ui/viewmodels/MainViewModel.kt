package com.twoplaytech.drbetting.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twoplaytech.drbetting.data.models.RateUs
import com.twoplaytech.drbetting.domain.usecases.RateUsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 22.11.21 14:17
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class MainViewModel @Inject constructor(private val rateUsUseCase: RateUsUseCase):ViewModel() {
    private val rateUsObserver = MutableLiveData<Pair<RateUs,Boolean>>()

    fun checkRateUs(){
        rateUsUseCase.getRateUs { rateUs, isCompleted ->
            rateUsObserver.postValue(Pair(rateUs,isCompleted))
        }
    }
    fun saveRateUs(rateUs: RateUs,isCompleted:Boolean = false){
       rateUsUseCase.saveRateUs(rateUs,isCompleted)
    }

    fun observeRateUs() = rateUsObserver
}