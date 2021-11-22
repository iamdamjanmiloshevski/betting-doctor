package com.twoplaytech.drbetting.domain.usecases

import com.twoplaytech.drbetting.data.models.RateUs
import com.twoplaytech.drbetting.domain.repository.Repository
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 22.11.21 14:18
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RateUsUseImpl @Inject constructor(repository: Repository) : RateUsUseCase,UseCase(repository){
    override fun getRateUs(callback: (RateUs, Boolean) -> Unit) {
        repository.getRateUs { rateUs, isCompleted ->
            Timber.e("${rateUs} ${isCompleted}")
            callback.invoke(rateUs,isCompleted)
        }
    }

    override fun saveRateUs(rateUs: RateUs, isCompleted: Boolean) {
        repository.saveRateUs(rateUs,isCompleted)
    }
}