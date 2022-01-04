package com.twoplaytech.drbetting.sportsanalyst.domain.usecases

import com.twoplaytech.drbetting.sportsanalyst.domain.repository.Repository

/*
    Author: Damjan Miloshevski 
    Created on 4.1.22 13:16
    Project: Dr.Betting
    © 2Play Tech  2022. All rights reserved
*/
abstract class UseCase(protected val repository: Repository)