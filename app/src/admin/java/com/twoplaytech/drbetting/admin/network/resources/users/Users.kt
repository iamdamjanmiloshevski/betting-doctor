package com.twoplaytech.drbetting.admin.network.resources.users

import io.ktor.resources.*
import kotlinx.serialization.Serializable

/*
    Author: Damjan Miloshevski 
    Created on 2.5.22 09:07
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Serializable
@Resource("users")
class Users {
    @Serializable
    @Resource("signin")
    class Login(val parent:Users = Users())
    @Serializable
    @Resource("token")
    class RefreshToken(val parent: Users = Users())
    @Serializable
    @Resource("notifications")
    class Notification(val parent:Users = Users())
}