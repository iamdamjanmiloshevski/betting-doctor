package com.twoplaytech.drbetting.common

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:06 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class FirestoreQueryLiveData(
    private val query: Query
) : LiveData<QuerySnapshot>(), EventListener<QuerySnapshot> {
    override fun onEvent(snapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        value = if (e != null) {
            null
        } else {
            snapshots
        }
    }

    private var listener: ListenerRegistration? = null
    override fun onActive() {
        super.onActive()
        listener = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        listener?.also {
            it.remove()
            listener = null
        }
    }
}