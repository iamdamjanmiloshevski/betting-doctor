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

package com.twoplaytech.drbetting.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.databinding.ItemSettingsBinding
import com.twoplaytech.drbetting.ui.settings.ISettingsItem
import com.twoplaytech.drbetting.ui.settings.SettingsActivity
import com.twoplaytech.drbetting.ui.settings.SettingsItem
import com.twoplaytech.drbetting.ui.viewholders.SettingsViewHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/20/21 8:43 PM
*/
class SettingsRecyclerViewAdapter() :
    RecyclerView.Adapter<SettingsViewHolder>() {
    private val items = listOf<SettingsItem>(
        SettingsItem.AppInfo(R.string.app_info, R.drawable.ic_about),
        SettingsItem.RateUs(R.string.rate_us, R.drawable.ic_rate_us),
        SettingsItem.PrivacyPolicy(R.string.privacy_policy, R.drawable.ic_privacy),
        SettingsItem.TermsOfUse(R.string.terms_of_use, R.drawable.ic_terms),
        SettingsItem.ThirdPartySoftware(R.string.third_party_software, R.drawable.ic_third_party),
        SettingsItem.NightMode(R.string.dark_mode,R.drawable.ic_night_mode),
        SettingsItem.Feedback(R.string.feedback,R.drawable.ic_outline_feedback_24),
        SettingsItem.Notifications(R.string.push_notifications,R.drawable.ic_baseline_notifications_none_24)
    )
    private var listener: OnSettingsItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsBinding.inflate(inflater, parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val item = items[position]
        with(item) {
            holder.binding.item = this as ISettingsItem
            holder.binding.root.setOnClickListener {
                listener?.onSettingsItemClick(this)
                    ?: throw RuntimeException("${SettingsActivity::class.java.simpleName} must implement OnSettingsItemClickListener")
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnSettingsItemClickListener(listener: OnSettingsItemClickListener) {
        this.listener = listener
    }
}