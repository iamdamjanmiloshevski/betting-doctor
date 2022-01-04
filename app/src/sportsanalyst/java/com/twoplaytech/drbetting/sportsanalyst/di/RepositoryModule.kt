package com.twoplaytech.drbetting.sportsanalyst.di

import com.twoplaytech.drbetting.sportsanalyst.domain.repository.Repository
import com.twoplaytech.drbetting.sportsanalyst.domain.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 12:14
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl):Repository
}