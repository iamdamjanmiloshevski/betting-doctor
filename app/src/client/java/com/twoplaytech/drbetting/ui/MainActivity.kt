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

package com.twoplaytech.drbetting.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.RateUs
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.ui.util.NotificationsManager
import com.twoplaytech.drbetting.ui.viewmodels.MainViewModel
import com.twoplaytech.drbetting.util.observeAppLaunchCount
import com.twoplaytech.drbetting.util.toGooglePlay
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener {
    private var navView: BottomNavigationView? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggleNotifications()
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView?.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
        bettingTipsViewModel.getAppLaunchCount()
    }

    private fun toggleNotifications() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val areNotificationsEnabled =
            sharedPrefs.getBoolean(getString(R.string.notifications_preferences_key), true)
        NotificationsManager.toggleNotifications(areNotificationsEnabled)
    }

    private fun observeAppLaunchCount(context: Context) {
        bettingTipsViewModel.observeAppLaunch().observe(this, { appLaunchCount ->
            if (appLaunchCount == 0) {
                context.observeAppLaunchCount()
            } else if (appLaunchCount > 0 && appLaunchCount % 10 == 0) {
                viewModel.checkRateUs()
            }
            bettingTipsViewModel.incrementAppLaunch()
        })
    }

    private fun observeRateUs() {
        viewModel.observeRateUs().observe(this, { pair ->
            val isCompleted = pair.second
            if (!isCompleted) {
                showRateUsDialog()
            }
        })
    }

    private fun showRateUsDialog() {
        MaterialDialog(this).show {
            cancelable(false)
            title(res = R.string.rate_us_title)
            message(res = R.string.rate_us_msg)
            positiveButton(res = R.string.rate_us_choice1, click = {
                this@MainActivity.toGooglePlay()
                viewModel.saveRateUs(RateUs.Rate_Us, true)
                dismiss()
            })
            neutralButton(res = R.string.rate_us_choice3, click = {
                viewModel.saveRateUs(RateUs.No, true)
                dismiss()
            })
            negativeButton(res = R.string.rate_us_choice2, click = {
                viewModel.saveRateUs(RateUs.MaybeLater, false)
                dismiss()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.navigation_football -> changeTheme(navView, Sport.Football)
            R.id.navigation_basketball -> changeTheme(navView, Sport.Basketball)
            R.id.navigation_tennis -> changeTheme(navView, Sport.Tennis)
            R.id.navigation_handball -> changeTheme(navView, Sport.Handball)
            R.id.navigation_volleyball -> changeTheme(navView, Sport.Volleyball)
        }
    }


    override fun observeData() {
        super.observeData()
        observeAppLaunchCount(this)
        observeAppTheme()
        observeRateUs()
    }
}