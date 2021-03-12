package com.twoplaytech.drbetting.ui.volleyball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:01 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@AndroidEntryPoint
class VolleyballFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initPager(
            listOf(
                VolleyballOlderFragment.getInstance(),
                VolleyballUpcomingFragment.getInstance()
            )
        )
        return binding.root
    }
}