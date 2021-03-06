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

package com.twoplaytech.drbetting

import android.content.Context
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.persistence.IPreferences.Companion.KEY_IS_FIRST_APP_LAUNCH
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.util.showDisclaimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDisclaimer(this)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_football,
                R.id.navigation_basketball,
                R.id.navigation_tennis,
                R.id.navigation_handball,
                R.id.navigation_volleyball
            )
        )
        navView?.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_football -> changeTheme(navView, Sport.FOOTBALL)
                R.id.navigation_basketball -> changeTheme(navView, Sport.BASKETBALL)
                R.id.navigation_tennis -> changeTheme(navView, Sport.TENNIS)
                R.id.navigation_handball -> changeTheme(navView, Sport.HANDBALL)
                R.id.navigation_volleyball -> changeTheme(navView, Sport.VOLLEYBALL)
            }
        }
    }

    private fun showDisclaimer(context: Context) {
        val isAppFirstLaunch = preferencesManager.getBoolean(KEY_IS_FIRST_APP_LAUNCH)
        if (isAppFirstLaunch) {
            context.showDisclaimer()
            preferencesManager.saveBoolean(KEY_IS_FIRST_APP_LAUNCH, false)
        }
    }

}