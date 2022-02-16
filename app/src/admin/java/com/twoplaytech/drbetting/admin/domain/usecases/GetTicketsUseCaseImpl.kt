package com.twoplaytech.drbetting.admin.domain.usecases

import com.twoplaytech.drbetting.admin.data.Resource
import com.twoplaytech.drbetting.admin.domain.repository.Repository
import com.twoplaytech.drbetting.data.models.Ticket
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 13:44
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
class GetTicketsUseCaseImpl @Inject constructor(repository: Repository):UseCase(repository),GetTicketsUseCase {
    override suspend fun getTickets(): Resource<List<Ticket>> {
        return repository.getTickets()
    }
}