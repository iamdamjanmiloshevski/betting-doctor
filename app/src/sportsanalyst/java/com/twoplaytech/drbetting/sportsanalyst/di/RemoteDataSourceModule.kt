package com.twoplaytech.drbetting.sportsanalyst.di

import com.twoplaytech.drbetting.sportsanalyst.data.datasource.RemoteDataSource
import com.twoplaytech.drbetting.sportsanalyst.data.datasource.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 12:12
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@Module
@InstallIn(ViewModelComponent::class)
interface RemoteDataSourceModule {
    @Binds
    fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl):RemoteDataSource
}