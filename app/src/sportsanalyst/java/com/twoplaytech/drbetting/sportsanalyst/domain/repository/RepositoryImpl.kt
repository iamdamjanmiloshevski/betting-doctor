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

package com.twoplaytech.drbetting.sportsanalyst.domain.repository

import com.twoplaytech.drbetting.data.datasource.LocalDataSource
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.datasource.RemoteDataSource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.util.toServerFormatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/*
    Author: Damjan Miloshevski 
    Created on 7.7.21 12:18
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class RepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) :
    Repository,
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun getTicketByDate(
        date: String,
        onSuccess: (Ticket) -> Unit,
        onError: (Message) -> Unit
    ) {
       launch(coroutineContext){
           remoteDataSource.getTicketByDate(date).catch { throwable ->
               Timber.e(throwable)
              sendErrorMessage(onError,throwable)
           }.collect { ticket ->
               onSuccess.invoke(ticket)
           }
       }
    }

    override suspend fun getTicketByDate(date: String): Resource<Ticket> {
        val response =  try {
            Resource.Loading(true)
            val ticket = remoteDataSource.getTicketByDate1(date)
            Resource.Success(ticket)
        }catch (e:Exception){
            Timber.e("Error while fetching ticket with date $date. Error -> ${e.localizedMessage}")
          return  Resource.Error(e.message,null)
        }
        Resource.Loading(false)
        return response
    }

    private fun sendErrorMessage(
        onError: (Message) -> Unit,
        throwable: Throwable
    ) {
        if (throwable is retrofit2.HttpException) {
            val response = throwable.response()
            response?.let { serverResponse ->
                try {
                    serverResponse.errorBody()?.let { errorBody ->
                        val errorMessage = MessageMapper.fromJson(errorBody.string())
                        onError.invoke(errorMessage)
                    } ?: onError.invoke(Message("Something went wrong", 0,Calendar.getInstance().toServerFormatDate()))
                } catch (e: Exception) {
                    onError.invoke(Message("Something went wrong", 0,Calendar.getInstance().toServerFormatDate()))
                }
            } ?: onError.invoke(Message("Something went wrong", 0,Calendar.getInstance().toServerFormatDate()))
        }
    }
}