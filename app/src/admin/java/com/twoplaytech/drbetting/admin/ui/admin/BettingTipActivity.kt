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

package com.twoplaytech.drbetting.admin.ui.admin

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.util.Constants
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_TIP
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_NEW
import com.twoplaytech.drbetting.data.entities.BettingTip
import com.twoplaytech.drbetting.data.entities.Sport
import com.twoplaytech.drbetting.data.entities.Status
import com.twoplaytech.drbetting.databinding.ActivityBettingTipBinding
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.util.getSportResource

class BettingTipActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBettingTipBinding
    private lateinit var navController: NavController
    private var viewType = VIEW_TYPE_NEW
    private var bettingTip: BettingTip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setContentView(binding.root)
        intent.extras.extractArguments()
        initUI()
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun initUI() {
        setSupportActionBar(binding.toolbar)
        binding.loadingView.isVisible = false
        navController = findNavController(R.id.nav_host_fragment_content_betting_tip)
        navController.setGraph(
            R.navigation.nav_graph_betting_tip, bundleOf(
                KEY_BETTING_TIP to bettingTip
            )
        )
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun observeData() {
        super.observeData()
        adminViewModel.observeForInsertedBettingTip().observe(this,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        finish()
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                        binding.loadingView.setText("Saving please wait")
                        binding.loadingView.isVisible = true
                    }
                }
            })
        adminViewModel.observeOnUpdatedTip().observe(this,{resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    finish()
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                    binding.loadingView.setText("Saving please wait")
                    binding.loadingView.isVisible = true
                }
            }
        })
    }

    override fun initBinding() {
        binding = ActivityBettingTipBinding.inflate(layoutInflater)
    }

    fun changeThemePerSport(sport: Sport?) {
        changeTheme(sport = sport, toolbar = binding.toolbar)
        binding.loadingView.setBackground(sport?.getSportResource()!!)
    }

    private fun Bundle?.extractArguments() {
        this?.let { args ->
            with(args) {
                val args = this.getBundle(Constants.KEY_BETTING_ARGS)
                args?.let { bettingArgs ->
                    viewType = bettingArgs.getInt(Constants.KEY_TYPE)
                    bettingTip = bettingArgs.getParcelable(KEY_BETTING_TIP)
                    when (viewType) {
                        VIEW_TYPE_NEW -> title = getString(R.string.new_betting_tip_title)
                        Constants.VIEW_TYPE_EDIT -> title =
                            getString(R.string.update_betting_tip_title)
                    }
                }
            }
        }
    }
}