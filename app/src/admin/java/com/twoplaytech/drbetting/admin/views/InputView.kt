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
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.ICustomView
import com.twoplaytech.drbetting.admin.common.TextWatcher
import com.twoplaytech.drbetting.databinding.ViewInputBinding
import java.text.SimpleDateFormat
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 17.5.21 14:33
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class InputView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs), ICustomView {
    private lateinit var binding: ViewInputBinding
    private var hint = 0
    private var title = 0
    private var type = 0
    private var gameTime: Date? = null

    init {
        val inflater = LayoutInflater.from(context)
        initBinding(inflater)
        extractAttributes(attrs)
        binding.tvHeader.text = context.getString(title)
        binding.inputHeader.hint = context.getString(hint)
        when (type) {
            0 -> {
                binding.etField.isEnabled = true
                binding.etField.inputType = InputType.TYPE_CLASS_TEXT
            }
            1 -> {
                binding.etField.inputType = InputType.TYPE_NULL
                binding.etField.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        context.showDatePicker()
                    }
                }
                binding.etField.setOnClickListener {
                    context.showDatePicker()
                }
            }
        }
        binding.etField.addTextChangedListener(TextWatcher(callback = {
            binding.inputHeader.error = null
        }))
    }

    override fun extractAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.InputView,
            0, 0
        ).apply {
            try {
                hint = getResourceId(R.styleable.InputView_hint, R.string.app_name)
                title = getResourceId(R.styleable.InputView_inputTitle, R.string.hint_league)
                type = getInteger(R.styleable.InputView_type, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun initBinding(inflater: LayoutInflater) {
        binding = ViewInputBinding.inflate(inflater, this, true)
    }

    fun setError(error: String? = null) {
        binding.inputHeader.error = error
        requestLayout()
    }

    fun <T : Any?> getInput(): T? {
        return when (type) {
            0 -> binding.etField.text.toString() as T
            1 -> gameTime as T
            else -> null
        }
    }

    private fun Context.showDatePicker() {
        MaterialDialog(context).show {
            dateTimePicker(show24HoursView = true) { _, dateTime ->
                // Use dateTime (Calendar)
                gameTime = dateTime.time
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                binding.etField.setText(sdf.format(dateTime.time))
            }
        }
    }
}