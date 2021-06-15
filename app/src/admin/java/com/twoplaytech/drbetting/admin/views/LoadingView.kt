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
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.admin.common.ICustomView
import com.twoplaytech.drbetting.databinding.ViewLoadingBinding

/*
    Author: Damjan Miloshevski 
    Created on 10.5.21 14:57
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class LoadingView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs),ICustomView {
    private lateinit var binding: ViewLoadingBinding

    init {
        val inflater = LayoutInflater.from(context)
        initBinding(inflater)
    }

    fun show(showView:Boolean){
        binding.progressView.visibility = if(showView) View.VISIBLE else View.GONE
    }

    fun setBackground(@DrawableRes backgroundResource:Int){
        binding.progressView.setBackgroundResource(backgroundResource)
    }

    fun setText(message:String){
        binding.tvLoadingMsg.text = message
    }

    override fun initBinding(inflater: LayoutInflater) {
        binding = ViewLoadingBinding.inflate(inflater, this, true)
    }
}