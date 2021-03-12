package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.twoplaytech.drbetting.databinding.FragmentBaseBinding
import com.twoplaytech.drbetting.ui.adapters.ChildFragmentViewPager2PagerAdapter

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:15 AM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
abstract class BaseFragment : Fragment(), IBaseView {
    protected lateinit var binding: FragmentBaseBinding
    protected lateinit var pagerAdapter: ChildFragmentViewPager2PagerAdapter
    override val viewModel:BettingTipsViewModel by activityViewModels()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
    }

    override fun initPager(screens: List<Fragment>) {
        pagerAdapter = ChildFragmentViewPager2PagerAdapter(this, screens)
        binding.vpTabs.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.vpTabs) { tab, position ->
            when (position) {
                0 -> tab.text = "Older"
                1 -> tab.text = "Upcoming"
            }
        }.attach()
    }
}