package com.twoplaytech.drbetting.sportsanalyst.domain.usecases

import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:12
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
interface GetTicketByDateUseCase {
  suspend  fun getTicketByDate(date:String): Resource<Ticket>
}