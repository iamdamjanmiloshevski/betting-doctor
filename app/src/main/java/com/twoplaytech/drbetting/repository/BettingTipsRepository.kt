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

package com.twoplaytech.drbetting.repository

import com.twoplaytech.drbetting.data.BettingTip2
import com.twoplaytech.drbetting.data.Sport2
import com.twoplaytech.drbetting.network.ApiManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 12:18
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class BettingTipsRepository @Inject constructor(private val apiManager: ApiManager) : IRepository2,
    CoroutineScope {
    override fun getBettingTips(): Flow<List<BettingTip2>> {
        return flow {
            val items = apiManager.api().getBettingTips()
            emit(items)
        }.flowOn(coroutineContext)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun getBettingTipsBySport(sport: Sport2, upcoming: Boolean): Flow<List<BettingTip2>> {
        return when (upcoming) {
            true -> flow {
                val items = apiManager.api().getUpcomingBettingTipsBySport(sport.value)
                emit(items)
            }.flowOn(coroutineContext)
            false -> flow {
                val items = apiManager.api().getOlderBettingTipsBySport(sport.value)
                emit(items)
            }.flowOn(coroutineContext)
        }
    }
}