package com.twoplaytech.drbetting.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.twoplaytech.drbetting.R
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
        initPager(listOf(FootballOlderFragment.getInstance(), FootballTodayFragment.getInstance()))
        return binding.root
    }

}