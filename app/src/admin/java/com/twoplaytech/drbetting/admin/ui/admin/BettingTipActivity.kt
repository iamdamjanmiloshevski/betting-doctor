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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.BaseAdminActivity
import com.twoplaytech.drbetting.admin.ui.common.BettingTipUiState
import com.twoplaytech.drbetting.admin.util.Constants
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_TIP
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_NEW
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.Status
import com.twoplaytech.drbetting.databinding.ActivityBettingTipBinding
import com.twoplaytech.drbetting.util.getSportResource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BettingTipActivity : BaseAdminActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBettingTipBinding
    private lateinit var navController: NavController
    private var viewType = VIEW_TYPE_NEW
    private var bettingTip: BettingTip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras.extractArguments()
        initBinding()
        initUI()
        observeData()
        setContentView(binding.root)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun initUI() {
        setSupportActionBar(binding.toolbar)
        binding.loadingView.isVisible = false
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_betting_tip) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(binding.toolbar)
        navController.setGraph(
            R.navigation.nav_graph_betting_tip, bundleOf(
                KEY_BETTING_TIP to bettingTip
            )
        )
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adminViewModel.bettingTipUiState.collectLatest { uiState->
                    when(uiState){
                        BettingTipUiState.Deleted -> finish()
                        is BettingTipUiState.Error -> {}
                        BettingTipUiState.Loading ->{
                            binding.loadingView.setText("Saving please wait")
                           binding.loadingView.isVisible = true
                        }
                        BettingTipUiState.Neutral -> {
                            /**
                             *
                             * do nothing
                             *
                             */
                        }
                        is BettingTipUiState.Success ->  finish()
                    }

                }
            }
        }

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
                val newArgs = this.getBundle(Constants.KEY_BETTING_ARGS)
                newArgs?.let { bettingArgs ->
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