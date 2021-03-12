package com.twoplaytech.drbetting.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.twoplaytech.drbetting.common.FirestoreQueryLiveData
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Resource
import com.twoplaytech.drbetting.repository.FirestoreRepository
import com.twoplaytech.drbetting.util.Constants.GAME_TIME
import com.twoplaytech.drbetting.util.Constants.SPORT
import com.twoplaytech.drbetting.util.asFirestoreQueryLiveData
import com.twoplaytech.drbetting.util.older
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:52 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@HiltViewModel
class BettingTipsViewModel @Inject constructor(private val repository: FirestoreRepository) :
    ViewModel() {
    private val olderTipsObserver = MutableLiveData<Resource<List<BettingType>>>()


    fun getUpcomingTips(type: String): FirestoreQueryLiveData {
        return repository.getBettingTips()
            .whereEqualTo(SPORT, type)
            .whereGreaterThan(GAME_TIME, older())
            .orderBy(GAME_TIME, Query.Direction.DESCENDING)
            .asFirestoreQueryLiveData()
    }

    fun getOlderTips(type: String) {
        olderTipsObserver.value = Resource.loading(null, null)
        val olderTips = mutableListOf<BettingType>()
        olderTips.clear()
        repository.getBettingTips()
            .whereEqualTo(SPORT, type)
            .whereLessThanOrEqualTo(GAME_TIME, older())
            .orderBy(GAME_TIME, Query.Direction.DESCENDING)
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val bettingTip = BettingType(document.data)
                    if (!olderTips.contains(bettingTip)) {
                        olderTips.add(bettingTip)
                    }
                }
            }.addOnCompleteListener { task ->
                if (task.isComplete) {
                    if (olderTips.isNotEmpty()) {
                        olderTipsObserver.value = Resource.success(null, olderTips)
                    } else {
                        olderTipsObserver.value = Resource.success("No data", listOf())
                    }
                }
            }.addOnFailureListener { exception ->
                olderTipsObserver.value = Resource.error(exception.localizedMessage, null)
            }
    }

    fun observeOnOlderTips() = olderTipsObserver
}