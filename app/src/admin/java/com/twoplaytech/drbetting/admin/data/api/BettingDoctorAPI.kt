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

package com.twoplaytech.drbetting.admin.data.api

import com.twoplaytech.drbetting.admin.data.models.AccessToken
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.UserInput
import retrofit2.http.*

/*
    Author: Damjan Miloshevski 
    Created on 9.9.21 14:32
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

    @PUT("betting-tips")
    suspend fun updateBettingTip(
        @Body bettingTip: BettingTip
    ): BettingTip

    @POST("betting-tips")
    suspend fun insertBettingTip(@Body bettingTip: BettingTip): BettingTip

    @DELETE("betting-tips/{id}")
    suspend fun deleteBettingTip(@Path("id") tipId: String): Message

    @POST("users/signin")
    suspend fun signIn(@Body userInput: UserInput): AccessToken

    @GET("users/tokens/refresh-token/{refreshToken}")
    suspend fun refreshToken(@Path("refreshToken") refreshToken:String): AccessToken
}