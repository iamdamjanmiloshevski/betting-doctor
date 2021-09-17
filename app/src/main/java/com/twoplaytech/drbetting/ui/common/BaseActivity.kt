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

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.persistence.SharedPreferencesManager
import com.twoplaytech.drbetting.ui.viewmodels.BettingTipsViewModel
import com.twoplaytech.drbetting.util.getRandomBackground
import com.twoplaytech.drbetting.util.getSportColor
import com.twoplaytech.drbetting.util.getSportDrawable
import com.twoplaytech.drbetting.util.getSportResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
    Author: Damjan Miloshevski 
    Created on 3/23/21 1:08 PM
*/
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), IBaseActivityView {
    @Inject
    lateinit var preferencesManager: SharedPreferencesManager

    protected val bettingTipsViewModel:BettingTipsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun changeTheme(
        navView: BottomNavigationView?,
        sport: Sport?,
        toolbar: Toolbar?,
        view: View?,
        appBarLayout: LinearLayout?
    ) {
        if (navView != null && sport != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, sport.getSportColor())
            navView.setBackgroundResource(sport.getSportResource())
        } else if (appBarLayout != null && sport != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, sport.getSportColor())
            appBarLayout?.setBackgroundResource(sport.getSportResource())
        } else if (toolbar != null && sport != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, sport.getSportColor())
            toolbar.background = sport.getSportDrawable(this)
        } else if (sport != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, sport.getSportColor())
        } else if (toolbar != null) {
            val backgrounds = this.getRandomBackground()
            toolbar.background = backgrounds.first
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, backgrounds.second)
        } else if (view != null) {
            val backgrounds = this.getRandomBackground()
            view.background = backgrounds.first
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, backgrounds.second)
        } else {
            val backgrounds = this.getRandomBackground()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, backgrounds.second)
        }
    }
}