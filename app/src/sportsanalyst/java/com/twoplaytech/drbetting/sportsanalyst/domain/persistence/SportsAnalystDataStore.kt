package com.twoplaytech.drbetting.sportsanalyst.domain.persistence

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 07:45
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface SportsAnalystDataStore {
    suspend fun <V:Any> save(key: Preferences.Key<V>, value:V)
    suspend fun <V:Any> fetch(key:Preferences.Key<V>):Flow<V>
}