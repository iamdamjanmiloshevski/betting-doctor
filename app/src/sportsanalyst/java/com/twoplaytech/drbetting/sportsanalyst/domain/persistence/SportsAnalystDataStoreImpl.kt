package com.twoplaytech.drbetting.sportsanalyst.domain.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 07:43
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/

@Singleton
class SportsAnalystDataStoreImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SportsAnalystDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override suspend fun <V : Any> save(key: Preferences.Key<V>, value: V) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    override suspend fun <V : Any> fetch(key: Preferences.Key<V>): Flow<V> =
        context.dataStore.data.mapNotNull { preferences ->
            preferences[key]
        }

    companion object {
        val PUSH_NOTIFICATIONS_KEY = booleanPreferencesKey("push_notifications")
    }
}