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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.twoplaytech.drbetting.data.Sport

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 11:17 AM
*/
interface IBaseView {
    fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )

    fun initPager(screens: List<Fragment>) {}
    val viewModel: BettingTipsViewModel
    fun initUI() {}
    fun setUpDataAdapter() {}
    fun changeTheme(@StringRes titleStringRes: Int, sport: Sport) {}
    fun setupToolbar(@StringRes titleRes: Int, sport: Sport) {}
    fun setupMenu(toolbar: Toolbar,@MenuRes menuRes:Int){}
    fun observeData(){}
}