package com.twoplaytech.drbetting.ui.handball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:01 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@AndroidEntryPoint
class HandballFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        changeTheme(R.string.heading_handball,Sport.HANDBALL)
        initPager(
            listOf(
                HandballOlderFragment.getInstance(),
                HandballUpcomingFragment.getInstance()
            )
        )
        return binding.root
    }
}