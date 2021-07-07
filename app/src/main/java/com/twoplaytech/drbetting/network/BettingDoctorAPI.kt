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

import com.twoplaytech.drbetting.data.BettingTip2
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 11:47
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface BettingDoctorAPI {
    @GET("betting_tips")
    suspend fun getBettingTips(): List<BettingTip2>

    @GET("betting_tips/{id}")
    suspend fun getBettingTipById(@Path("id") tipId: String): BettingTip2

    @GET("betting_tips/{sport}/upcoming")
    suspend fun getUpcomingBettingTipsBySport(@Path("sport") sport: String): List<BettingTip2>

    @GET("betting_tips/{sport}/older")
    suspend fun getOlderBettingTipsBySport(@Path("sport") sport: String): List<BettingTip2>

    @PUT("betting_tips/{id}")
    suspend fun updateBettingTip(@Path("id") tipId: String): BettingTip2

    @DELETE("betting_tips/{id}")
    suspend fun deleteBettingTip(@Path("id") tipId: String)

    @DELETE("betting_tips")
    suspend fun deleteBettingTips()

}