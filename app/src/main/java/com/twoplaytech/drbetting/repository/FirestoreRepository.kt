package com.twoplaytech.drbetting.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.twoplaytech.drbetting.util.Constants.TIPS
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:09 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class FirestoreRepository @Inject constructor(private val db: FirebaseFirestore) : IRepository {
    override fun getBettingTips(): CollectionReference {
        return db.collection(TIPS)
    }
}