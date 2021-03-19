package com.twoplaytech.drbetting.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FootballFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        changeTheme(R.string.heading_football,Sport.FOOTBALL)
        initPager(
            listOf(
                FootballOlderFragment.getInstance(),
                FootballUpcomingFragment.getInstance()
            )
        )
        return binding.root
    }

}