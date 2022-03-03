package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 13:44
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
class GetTicketsUseCaseImpl @Inject constructor(repository: Repository):UseCase(repository),GetTicketsUseCase {
    override suspend fun getTickets(): Flow<List<Ticket>> {
        return flow { emit(repository.getTickets()) }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTicketById(id: String): Flow<Ticket> {
       return flow { emit(repository.getTicketById(id)) }.flowOn(Dispatchers.IO)
    }

}