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
import com.twoplaytech.drbetting.common.ISettingsItem
import com.twoplaytech.drbetting.data.SettingsItem
import com.twoplaytech.drbetting.databinding.ItemSettingsBinding
import com.twoplaytech.drbetting.ui.viewholders.SettingsViewHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/20/21 8:43 PM
*/
class SettingsRecyclerViewAdapter():RecyclerView.Adapter<SettingsViewHolder>() {
    private val items = listOf<SettingsItem>(
        SettingsItem.AboutUs("AboutUs", R.drawable.ic_about),
        SettingsItem.Contact("Contact",R.drawable.ic_contact),
        SettingsItem.Feedback("Feedback",R.drawable.ic_about),
        SettingsItem.RateUs("Rate Us",R.drawable.ic_rate_us),
        SettingsItem.PrivacyPolicy("Privacy Policy",R.drawable.ic_privacy),
        SettingsItem.TermsOfUse("Terms of Use",R.drawable.ic_terms),
        SettingsItem.ThirdPartySoftware("Third Party software",R.drawable.ic_about),
        SettingsItem.Language("Language",R.drawable.ic_about)
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingsBinding.inflate(inflater,parent,false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        val item = items[position]
        holder.binding.item = item as ISettingsItem
    }

    override fun getItemCount(): Int {
        return items.size
    }
}