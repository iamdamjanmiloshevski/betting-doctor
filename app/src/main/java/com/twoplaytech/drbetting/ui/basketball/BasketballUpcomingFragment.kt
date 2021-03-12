package com.twoplaytech.drbetting.ui.basketball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.ui.common.BaseChildFragment
import com.twoplaytech.drbetting.ui.football.FootballUpcomingFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:36 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@AndroidEntryPoint
class BasketballUpcomingFragment : BaseChildFragment() {
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
        binding.noDataView.setVisible(false)
        setUpDataAdapter()
        requestTodayData(Sport.BASKETBALL)
    }

    companion object {
        fun getInstance(): FootballUpcomingFragment {
            return FootballUpcomingFragment()
        }
    }
}