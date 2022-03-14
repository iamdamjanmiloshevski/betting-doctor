package com.twoplaytech.drbetting.sportsanalyst.di

import com.twoplaytech.drbetting.sportsanalyst.domain.persistence.SportsAnalystDataStore
import com.twoplaytech.drbetting.sportsanalyst.domain.persistence.SportsAnalystDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 07:52
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {
    @Singleton
    @Binds
    fun bindsDataStore(dataStoreImpl: SportsAnalystDataStoreImpl): SportsAnalystDataStore
}