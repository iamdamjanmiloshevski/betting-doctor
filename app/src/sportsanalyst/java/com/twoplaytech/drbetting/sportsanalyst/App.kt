package com.twoplaytech.drbetting.sportsanalyst

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.twoplaytech.drbetting.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/*
    Author: Damjan Miloshevski 
    Created on 3.1.22 17:21
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
@HiltAndroidApp
class App:Application(){
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Stetho.initializeWithDefaults(this)
        AndroidThreeTen.init(this)
    }
}