package com.twoplaytech.drbetting.domain.usecases

import com.twoplaytech.drbetting.domain.repository.Repository
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 10.11.21 15:49
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class ChangeThemeUseCaseImpl @Inject constructor(repository: Repository) : UseCase(repository),ChangeThemeUseCase {
    override fun getAppTheme(callback: (Int) -> Unit) {
        repository.getAppTheme { callback.invoke(it) }
    }

    override fun saveAppTheme(theme: Int) {
        repository.saveAppTheme(theme)
    }
}