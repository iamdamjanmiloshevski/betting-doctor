package com.twoplaytech.drbetting.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:08 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
}