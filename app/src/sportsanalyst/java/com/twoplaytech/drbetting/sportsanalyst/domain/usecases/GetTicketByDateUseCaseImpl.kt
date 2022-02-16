package com.twoplaytech.drbetting.sportsanalyst.domain.usecases

import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.domain.repository.Repository
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:13
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
class GetTicketByDateUseCaseImpl @Inject constructor(repository: Repository) :
    GetTicketByDateUseCase, UseCase(repository) {
    override suspend fun getTicketByDate(date: String): Resource<Ticket> {
        return repository.getTicketByDate(date)
    }
}