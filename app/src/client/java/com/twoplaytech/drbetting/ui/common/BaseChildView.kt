package com.twoplaytech.drbetting.ui.common

/*
    Author: Damjan Miloshevski 
    Created on 29.4.22 14:34
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
interface BaseChildView {
    fun observeData()
    fun setUpDataAdapter()
    fun showLoader(show: Boolean)
}