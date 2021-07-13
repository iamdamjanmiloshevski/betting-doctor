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

package com.twoplaytech.drbetting.network

import com.twoplaytech.drbetting.data.*
import retrofit2.http.*

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 11:47
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface BettingDoctorAPI {
    @GET("betting-tips")
    suspend fun getBettingTips(): List<BettingTip>

    @GET("betting-tips/{id}")
    suspend fun getBettingTipById(@Path("id") tipId: String): BettingTip

    @GET("betting-tips/{sport}/upcoming")
    suspend fun getUpcomingBettingTipsBySport(@Path("sport") sport: Sport): List<BettingTip>

    @GET("betting-tips/{sport}/older")
    suspend fun getOlderBettingTipsBySport(@Path("sport") sport: Sport): List<BettingTip>

    @PUT("betting-tips/{id}")
    suspend fun updateBettingTip(@Path("id") tipId: String): BettingTip

    @DELETE("betting-tips/{id}")
    suspend fun deleteBettingTip(@Path("id") tipId: String)

    @DELETE("betting-tips")
    suspend fun deleteBettingTips()

    @POST("users/register")
    suspend fun register(@Body userInput: UserInput):Message

    @POST("users/signin")
    suspend fun signIn(@Body userInput: UserInput):AccessToken

}