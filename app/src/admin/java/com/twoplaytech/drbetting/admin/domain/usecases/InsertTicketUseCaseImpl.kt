package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 22.2.22 15:57
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
class InsertTicketUseCaseImpl @Inject constructor(repository: Repository) : InsertTicketUseCase,
    UseCase(repository) {
    override suspend fun insertTicket(ticket: TicketInput): Flow<Ticket> =
        flow { emit(repository.insertTicket(ticket)) }.flowOn(Dispatchers.IO)
}