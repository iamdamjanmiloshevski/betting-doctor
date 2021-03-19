package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.twoplaytech.drbetting.data.Sport

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:17 AM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
interface IBaseView {
    fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )

    fun initPager(screens: List<Fragment>) {}
    val viewModel: BettingTipsViewModel
    fun initUI() {}
    fun setUpDataAdapter() {}
    fun changeTheme(@StringRes titleStringRes: Int, sport: Sport) {}
    fun setToolbar(@StringRes titleRes: Int, sport: Sport) {}
}