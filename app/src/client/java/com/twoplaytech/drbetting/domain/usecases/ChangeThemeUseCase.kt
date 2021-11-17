package com.twoplaytech.drbetting.domain.usecases

/*
    Author: Damjan Miloshevski 
    Created on 10.11.21 15:48
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface ChangeThemeUseCase {
    fun getAppTheme(callback: (Int) -> Unit)
    fun saveAppTheme(theme: Int)
}