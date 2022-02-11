package com.twoplaytech.drbetting.sportsanalyst.domain.usecases

import com.twoplaytech.drbetting.data.models.Message
import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
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
    override fun getTicketByDate(
        date: String,
        onSuccess: (Ticket) -> Unit,
        onError: (Message) -> Unit
    ) {
        repository.getTicketByDate(
            date,
            onSuccess = { onSuccess.invoke(it) },
            onError = { onError.invoke(it) })
    }

    override suspend fun getTicketByDate1(date: String): Resource<Ticket> {
        return repository.getTicketByDate(date)
    }
}