package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup

/*
    Author: Damjan Miloshevski 
    Created on 29.4.22 14:35
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface BindingInitializer {
    fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )
}