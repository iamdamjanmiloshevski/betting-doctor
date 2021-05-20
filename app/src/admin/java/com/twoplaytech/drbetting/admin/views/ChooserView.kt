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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.ICustomView
import com.twoplaytech.drbetting.admin.common.OnDropdownItemSelectedListener
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.TypeStatus
import com.twoplaytech.drbetting.databinding.ViewChooserBinding

/*
    Author: Damjan Miloshevski 
    Created on 17.5.21 15:03
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class ChooserView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs),
    ICustomView,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: ViewChooserBinding
    private var title = 0
    private var items = 0
    private var listener: OnDropdownItemSelectedListener? = null

    init {
        val inflater = LayoutInflater.from(context)
        initBinding(inflater)
        extractAttributes(attrs)
        binding.tvHeader.text = context.getString(title)
        ArrayAdapter.createFromResource(
            context,
            items,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spItems.adapter = adapter
        }
        binding.spItems.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (items == R.array.sports) {
            when (position) {
                0 -> listener?.onSportClicked(Sport.FOOTBALL)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                1 -> listener?.onSportClicked(Sport.BASKETBALL)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                2 -> listener?.onSportClicked(Sport.TENNIS)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                3 -> listener?.onSportClicked(Sport.HANDBALL)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                4 -> listener?.onSportClicked(Sport.VOLLEYBALL)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
            }
        } else {
            when (position) {
                0 -> listener?.onStatusClicked(TypeStatus.WON)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                1 -> listener?.onStatusClicked(TypeStatus.LOST)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
                2 -> listener?.onStatusClicked(TypeStatus.UNKNOWN)
                    ?: throw RuntimeException("${ChooserView::class.simpleName} must implement OnDropdownItemSelectedListener")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun extractAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ChooserView,
            0, 0
        ).apply {
            try {
                title = getResourceId(R.styleable.ChooserView_chooserTitle, R.string.app_name)
                items = getResourceId(R.styleable.ChooserView_items, R.array.sports)
            } finally {
                recycle()
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater) {
        binding = ViewChooserBinding.inflate(inflater, this, true)
    }

    fun setOnDropDownItemSelectedListener(listener: OnDropdownItemSelectedListener) {
        this.listener = listener
    }
    fun setSelection(idx:Int){
        binding.spItems.setSelection(idx)
        invalidate()
        requestLayout()
    }
}