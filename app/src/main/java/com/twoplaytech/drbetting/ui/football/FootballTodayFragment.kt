package com.twoplaytech.drbetting.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.ui.common.BaseChildFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:36 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@AndroidEntryPoint
class FootballTodayFragment : BaseChildFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initUI()
        return binding.root
    }

    override fun initUI() {
        setUpDataAdapter()
        requestTodayData(Sport.FOOTBALL)
    }

    companion object {
        fun getInstance(): FootballTodayFragment {
            return FootballTodayFragment()
        }
    }
}