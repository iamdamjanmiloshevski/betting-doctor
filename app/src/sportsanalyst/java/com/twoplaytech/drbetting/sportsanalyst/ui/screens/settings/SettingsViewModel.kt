package com.twoplaytech.drbetting.sportsanalyst.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoplaytech.drbetting.sportsanalyst.domain.persistence.SportsAnalystDataStore
import com.twoplaytech.drbetting.sportsanalyst.domain.persistence.SportsAnalystDataStoreImpl.Companion.PUSH_NOTIFICATIONS_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 07:56
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@HiltViewModel
class SettingsViewModel @Inject constructor(private val dataStore: SportsAnalystDataStore) :
    ViewModel() {
        private val _pushNotificationsState:MutableStateFlow<Boolean> = MutableStateFlow(true)
        val pushNotificationsState:StateFlow<Boolean> = _pushNotificationsState

    init {
        getSettings()
    }

        private fun getSettings(){
            viewModelScope.launch {
                dataStore.fetch(PUSH_NOTIFICATIONS_KEY)
                    .catch { e ->
                        Timber.e(e)
                        _pushNotificationsState.value = false
                    }
                    .collect {_pushNotificationsState.value = it}
            }
        }
    fun enableNotifications(enabled:Boolean){
      viewModelScope.launch {
          dataStore.save(PUSH_NOTIFICATIONS_KEY,enabled)
      }
    }
}