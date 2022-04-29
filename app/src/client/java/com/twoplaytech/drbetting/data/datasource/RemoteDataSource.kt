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

package com.twoplaytech.drbetting.data.datasource

import com.twoplaytech.drbetting.data.common.Either
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.FeedbackMessage
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.data.models.Sport
import kotlinx.coroutines.flow.Flow

/*
    Author: Damjan Miloshevski 
    Created on 23.8.21 15:23
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface RemoteDataSource {
    suspend fun getBettingTips(): Flow<List<BettingTip>>
    suspend fun getBettingTipsKtor():Either<Message,List<BettingTip>>
    suspend fun getBettingTipsBySport(
        sport: Sport,
        upcoming: Boolean = false
    ): Flow<List<BettingTip>>

    suspend fun getBettingTipsBySportKtor(
        sport: Sport,
        upcoming: Boolean = false):Either<Message,List<BettingTip>>
    suspend fun getBettingTipById(
        id: String
    ): Flow<BettingTip>

    suspend fun getBettingTipByIdKtor(id: String):Either<Message,BettingTip>
    suspend fun sendFeedback(feedbackMessage: FeedbackMessage):Flow<FeedbackMessage>
    suspend fun sendFeedbackKtor(feedbackMessage: FeedbackMessage):Either<Message,FeedbackMessage>
}