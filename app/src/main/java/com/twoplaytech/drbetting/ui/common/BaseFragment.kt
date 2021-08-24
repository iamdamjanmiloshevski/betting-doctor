/*
 * MIT License
 *
 * Copyright (c)  2021 Damjan Miloshevski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.twoplaytech.drbetting.ui.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.tabs.TabLayoutMediator
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.entities.Sport
import com.twoplaytech.drbetting.databinding.FragmentBaseBinding
import com.twoplaytech.drbetting.ui.adapters.ChildFragmentViewPager2PagerAdapter
import com.twoplaytech.drbetting.ui.settings.SettingsActivity
import com.twoplaytech.drbetting.util.getSportResource
import com.twoplaytech.drbetting.util.showDisclaimer

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:15 AM
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
        setupToolbar(titleStringRes, sport)
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

    override fun setupToolbar(titleRes: Int, sport: Sport) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.title = getString(titleRes)
        toolbar.setBackgroundResource(sport.getSportResource())
        toolbar.setupWithNavController(navController, appBarConfiguration)
        setupMenu(toolbar, R.menu.main_menu)
    }

    override fun setupMenu(toolbar: Toolbar, @MenuRes menuRes: Int) {
        toolbar.inflateMenu(menuRes)
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
                true
            }
            R.id.action_admin_settings -> {
                activity?.openAdmin()
                true
            }
            R.id.action_disclaimer -> {
                requireContext().showDisclaimer()
                true
            }
            else -> false
        }
    }
}

 private fun Activity.openAdmin() {
    val packageName = "com.twoplaytech.drbetting.admin"
    var intent = this.packageManager.getLaunchIntentForPackage(packageName)
    if (intent == null) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$packageName")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}
