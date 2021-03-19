/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package com.twoplaytech.drbetting.ui.common

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
        val colorInt = ContextCompat.getColor(requireContext(), R.color.white)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            toolbar.menu.findItem(R.id.action_disclaimer).iconTintList =
                ColorStateList.valueOf(colorInt)
        }
        toolbar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT)
                    .show()
                true
            }
            else -> false
        }
    }
}