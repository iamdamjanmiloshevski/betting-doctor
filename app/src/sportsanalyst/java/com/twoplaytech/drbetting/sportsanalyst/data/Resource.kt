package com.twoplaytech.drbetting.sportsanalyst.data

/*
    Author: Damjan Miloshevski 
    Created on 11.2.22 15:37
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
sealed class Resource<T>(val data:T? = null, val message:String? = null){
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message: String?,data:T?=null):Resource<T>(data,message)
    class Loading<T>(data:T?=null):Resource<T>(data)
}
