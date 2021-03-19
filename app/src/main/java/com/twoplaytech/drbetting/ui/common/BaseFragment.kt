package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.databinding.FragmentBaseBinding
import com.twoplaytech.drbetting.ui.adapters.ChildFragmentViewPager2PagerAdapter
import com.twoplaytech.drbetting.util.getSportResource

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:15 AM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
abstract class BaseFragment : Fragment(), IBaseView, Toolbar.OnMenuItemClickListener {
    protected lateinit var binding: FragmentBaseBinding
    private lateinit var pagerAdapter: ChildFragmentViewPager2PagerAdapter
    override val viewModel: BettingTipsViewModel by activityViewModels()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
    }

    override fun changeTheme(@StringRes titleStringRes: Int, sport: Sport) {
        binding.tabs.setBackgroundResource(sport.getSportResource())
        setToolbar(titleStringRes, sport)
    }

    override fun initPager(screens: List<Fragment>) {
        pagerAdapter = ChildFragmentViewPager2PagerAdapter(this, screens)
        binding.vpTabs.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.vpTabs) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.older_tab)
                1 -> tab.text = getString(R.string.upcoming_tab)
            }
        }.attach()
    }

    override fun setToolbar(titleRes: Int, sport: Sport) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.title = getString(titleRes)
        toolbar.setBackgroundResource(sport.getSportResource())
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings ->{Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT)
                .show()
                true
            }
            else -> false
        }
    }
}