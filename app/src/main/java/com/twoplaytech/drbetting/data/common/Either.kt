package com.twoplaytech.drbetting.data.common

/*
    Author: Damjan Miloshevski 
    Created on 26.4.22 17:01
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
/**
 * Either class can be used to abstract API responses depending on the response
 * error vs success. It's a generic class so it can be customized per developer's needs
 *
 * @param E - error response type
 * @param R - success response type
 */
sealed class Either<out E,out R>{
    data class Failure<E>(val message:E):Either<E, Nothing>()
    data class Response<R>(val data:R):Either<Nothing,R>()
}
