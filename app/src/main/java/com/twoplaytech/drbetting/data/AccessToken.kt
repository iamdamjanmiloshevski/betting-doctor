package com.twoplaytech.drbetting.data

/*
    Author: Damjan Miloshevski 
    Created on 24/06/2021
    Project: betting-doctor
    © 2Play Technologies  2021. All rights reserved
*/
data class AccessToken(val token: String, val refreshToken: String, val expiresIn: Long)
