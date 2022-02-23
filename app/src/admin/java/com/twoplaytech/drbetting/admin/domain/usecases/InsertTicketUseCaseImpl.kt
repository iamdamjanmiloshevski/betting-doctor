package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.data.models.TicketInput
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Ticket
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 22.2.22 15:57
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
class InsertTicketUseCaseImpl @Inject constructor(repository: Repository) : InsertTicketUseCase,
    UseCase(repository) {
    override suspend fun insertTicket(ticket: TicketInput): Resource<Ticket> =
        repository.insertTicket(ticket)
}