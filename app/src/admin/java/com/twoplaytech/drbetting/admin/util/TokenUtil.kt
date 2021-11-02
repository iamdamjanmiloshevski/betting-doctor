package com.twoplaytech.drbetting.admin.util

import com.auth0.android.jwt.JWT
import com.twoplaytech.drbetting.admin.data.models.AccessToken
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 2.11.21 13:31
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
fun AccessToken.hasExpired():Boolean{
    val token = JWT(this.refreshToken)
    return token.expiresAt?.before(Date(System.currentTimeMillis())) ?: true
}