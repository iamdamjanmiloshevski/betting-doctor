package com.twoplaytech.drbetting.sportsanalyst.di

import com.twoplaytech.drbetting.sportsanalyst.domain.usecases.GetTicketByDateUseCase
import com.twoplaytech.drbetting.sportsanalyst.domain.usecases.GetTicketByDateUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:29
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@Module
@InstallIn(ViewModelComponent::class)
interface UseCasesModule {
    @Binds
    fun bindGetTicketByDateUseCase(getTicketByDateUseCaseImpl: GetTicketByDateUseCaseImpl): GetTicketByDateUseCase
}