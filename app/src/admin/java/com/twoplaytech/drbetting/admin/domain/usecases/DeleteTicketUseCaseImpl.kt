package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3.3.22 13:35
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
class DeleteTicketUseCaseImpl @Inject constructor(repository: Repository) : UseCase(repository),
    DeleteTicketUseCase {
    override suspend fun deleteTicket(id: String): Flow<Message> =
        flow { emit(repository.deleteTicketById(id)) }.flowOn(Dispatchers.IO)
}