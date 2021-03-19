package com.twoplaytech.drbetting.ui.tennis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TennisFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        changeTheme(R.string.heading_tennis,Sport.TENNIS)
        initPager(
            listOf(
                TennisOlderFragment.getInstance(),
                TennisUpcomingFragment.getInstance()
            )
        )
        return binding.root
    }
}