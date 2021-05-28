/*
 * MIT License
 *
 * Copyright (c)  2021 Damjan Miloshevski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.twoplaytech.drbetting.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.util.Constants.TIPS
import timber.log.Timber
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:09 PM

*/
class FirestoreRepository @Inject constructor(private val db: FirebaseFirestore) : IRepository {
    override fun getBettingTips(): CollectionReference {
        return db.collection(TIPS)
    }

    override fun saveBettingTip(
        bettingType: BettingType,
        successCallback: (String) -> Unit,
        failureCallback: (String) -> Unit
    ) {
        val id = db.collection(TIPS).document().id
        bettingType.id = id
        db.collection(TIPS).document(id).set(bettingType.mapify()).addOnCompleteListener {
            if (it.isSuccessful) {
                successCallback.invoke("Betting tip added successfully!" )
            }
        }.addOnFailureListener {
            Timber.e(it)
            failureCallback.invoke(it.message!!)
        }
    }

    override fun updateBettingTip(
        bettingType: BettingType,
        successCallback: (String) -> Unit,
        failureCallback: (String) -> Unit
    ) {
        db.collection(TIPS).document(bettingType.id).update(bettingType.mapify())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    successCallback.invoke("Betting tip ${bettingType.id} updated successfully!" )
                }
            }.addOnFailureListener {
            Timber.e(it)
            failureCallback.invoke(it.message!!)
        }
    }
}