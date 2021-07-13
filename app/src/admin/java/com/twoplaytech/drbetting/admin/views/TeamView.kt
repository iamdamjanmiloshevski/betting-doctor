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

package com.twoplaytech.drbetting.admin.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.ICustomView
import com.twoplaytech.drbetting.common.TextWatcher
import com.twoplaytech.drbetting.data.Team
import com.twoplaytech.drbetting.databinding.ItemTeamBinding

/*
    Author: Damjan Miloshevski 
    Created on 14.5.21 15:43
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class TeamView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet), ICustomView {

    private lateinit var binding: ItemTeamBinding
    private var isExpanded = false
    private var hasError = false

    init {
        val inflater = LayoutInflater.from(context)
        initBinding(inflater)
        expand()
        binding.teamView.setOnClickListener {
            if (isExpanded) collapse() else expand()
        }
        binding.etTeamName.addTextChangedListener(TextWatcher(callback = {
            binding.etTeamName.error = null
            hasError = false
            requestLayout()
        }))
    }


    private fun expand() {
        binding.tvTeamName.visibility = View.VISIBLE
        binding.etTeamName.visibility = View.VISIBLE
        binding.tvTeamLogo.visibility = View.VISIBLE
        binding.etTeamLogo.visibility = View.VISIBLE
        binding.ivError.visibility = View.GONE
        binding.ivExpander.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        binding.tvHeader.text = null
        isExpanded = true
    }

    private fun collapse() {
        val header = binding.etTeamName.text.toString()
        binding.tvHeader.text = header
        binding.tvTeamName.visibility = View.GONE
        binding.etTeamName.visibility = View.GONE
        binding.tvTeamLogo.visibility = View.GONE
        binding.etTeamLogo.visibility = View.GONE
        binding.ivError.visibility = if (hasError) View.VISIBLE else View.GONE
        binding.ivExpander.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        isExpanded = false
    }

    fun setError(@StringRes errorRes: Int?) {
        if (errorRes != null) {
            hasError = true
            binding.etTeamName.error = context.getString(errorRes)
        } else {
            hasError = false
            binding.etTeamName.error = null
        }
    }

    fun populate(team:Team?){
        team?.let {
            binding.etTeamName.setText(it.name)
            binding.etTeamLogo.setText(it.logo)
        }
    }

    fun getTeamName(): String = binding.etTeamName.text.toString()
    fun getTeamLogoUrl(): String? = binding.etTeamLogo.text.toString()

    override fun initBinding(inflater: LayoutInflater) {
        binding = ItemTeamBinding.inflate(inflater, this, true)
    }


}