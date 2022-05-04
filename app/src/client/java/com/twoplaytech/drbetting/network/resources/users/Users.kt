package com.twoplaytech.drbetting.network.resources.users

import io.ktor.resources.*
import kotlinx.serialization.Serializable


/*
    Author: Damjan Miloshevski 
    Created on 29.4.22 15:00
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/

@Serializable
@Resource("/users")
class Users {
    @Serializable
    @Resource("feedback")
    class Feedback(val parent: Users = Users())
}