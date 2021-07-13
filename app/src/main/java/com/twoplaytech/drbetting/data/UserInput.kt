package com.twoplaytech.drbetting.data

/*
    Author: Damjan Miloshevski 
    Created on 20/06/2021
    Project: betting-doctor
    © 2Play Technologies  2021. All rights reserved
*/
data class UserInput(
    val email: String,
    val password: String,
    var name: String = "",
    var surname: String = "",
    var avatarUrl: String? = null,
    var newPassword:String? = null,
    var role: UserRole = UserRole.CUSTOMER
)