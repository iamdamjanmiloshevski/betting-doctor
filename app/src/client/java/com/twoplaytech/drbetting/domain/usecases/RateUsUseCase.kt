package com.twoplaytech.drbetting.domain.usecases

import com.twoplaytech.drbetting.data.models.RateUs

/*
    Author: Damjan Miloshevski 
    Created on 22.11.21 14:17
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface RateUsUseCase {
    fun getRateUs(callback: (RateUs,Boolean) -> Unit)
    fun saveRateUs(rateUs: RateUs,isCompleted:Boolean = false)
}