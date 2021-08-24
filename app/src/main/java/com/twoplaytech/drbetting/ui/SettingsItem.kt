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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/*
    Author: Damjan Miloshevski 
    Created on 3/20/21 8:23 PM
*/
sealed class SettingsItem {
    data class AppInfo(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class Contact(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class Feedback(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class RateUs(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class PrivacyPolicy(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class TermsOfUse(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class ThirdPartySoftware(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()

    data class Language(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()
    data class Notifications(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
        ISettingsItem, SettingsItem()
    data class NightMode(@StringRes override val name: Int, @DrawableRes override val icon: Int) :
    ISettingsItem, SettingsItem()
}
