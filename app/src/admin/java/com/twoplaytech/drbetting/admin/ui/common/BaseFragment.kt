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

package com.twoplaytech.drbetting.admin.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.databinding.FragmentBaseBinding
import com.twoplaytech.drbetting.ui.common.IBaseView
import com.twoplaytech.drbetting.ui.viewmodels.BettingTipsViewModel
import com.twoplaytech.drbetting.util.getSportResource

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:15 AM
*/
abstract class BaseFragment : Fragment(), IBaseView {
    protected lateinit var binding: FragmentBaseBinding
    override val viewModel: BettingTipsViewModel by activityViewModels()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentBaseBinding.inflate(inflater, container, false)
    }

    override fun changeTheme(@StringRes titleStringRes: Int, sport: Sport) {
        binding.tabs.setBackgroundResource(sport.getSportResource())
        setupToolbar(titleStringRes, sport)
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
}
