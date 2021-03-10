package com.twoplaytech.drbetting.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:40 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class ChildFragmentViewPager2PagerAdapter(fragment: Fragment, private val screens: List<Fragment>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        return screens[position]
    }
}