package com.twoplaytech.drbetting.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.twoplaytech.drbetting.common.FirestoreQueryLiveData
import com.twoplaytech.drbetting.repository.FirestoreRepository
import com.twoplaytech.drbetting.ui.basketball.BettingType
import com.twoplaytech.drbetting.util.Constants.GAME_TIME
import com.twoplaytech.drbetting.util.Constants.SPORT
import com.twoplaytech.drbetting.util.asFirestoreQueryLiveData
import com.twoplaytech.drbetting.util.yesterday
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(val repository: FirestoreRepository) : ViewModel() {
    private var olderTipsLiveData: MutableLiveData<List<BettingType?>> = MutableLiveData()
    private var olderTips: MutableList<BettingType?> = mutableListOf()

    fun getTodayTips(type: String): FirestoreQueryLiveData {
        return repository.getBettingTips()
            .whereEqualTo(SPORT, type)
            .whereGreaterThan(GAME_TIME, yesterday())
            .orderBy(GAME_TIME, Query.Direction.DESCENDING)
            .asFirestoreQueryLiveData()
    }
}