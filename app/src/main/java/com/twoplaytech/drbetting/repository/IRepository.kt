package com.twoplaytech.drbetting.repository

import com.google.firebase.firestore.CollectionReference

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:07 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface IRepository {
    fun getBettingTips(): CollectionReference
}