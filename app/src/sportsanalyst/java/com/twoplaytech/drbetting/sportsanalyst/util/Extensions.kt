package com.twoplaytech.drbetting.sportsanalyst.util

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 09:04
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
fun Throwable.toUserMessage():String{
    this.message?.let {
        when {
            it.contains("HTTP 404") -> return  "Apologies we have nothing for you"
            it == "No betting tips available" -> return "No betting tips available"
            else -> "Something went wrong"
        }
    }
    return  "Something went wrong"
}