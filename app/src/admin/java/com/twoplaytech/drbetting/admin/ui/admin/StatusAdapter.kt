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

package com.twoplaytech.drbetting.admin.ui.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.databinding.ItemStatusBinding


/*
    Author: Damjan Miloshevski 
    Created on 21.5.21 16:57
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class StatusAdapter(
    context: Context, textViewResourceId: Int,
    val objects: Array<String>, val type: Int
) :
    ArrayAdapter<Any?>(context, textViewResourceId, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemStatusBinding.inflate(inflater, parent, false)

        objects?.let {
            binding.text1.text = objects[position]
        }
        when(type){
            0 -> {
                binding.tvStatusIcon.setImageResource(
                    when (position) {
                        0 -> R.drawable.tip_won
                        1 -> R.drawable.tip_lost
                        2 -> R.drawable.tip_unknown
                        else -> R.drawable.tip_unknown
                    }
                )
            }
            1-> {
                binding.tvStatusIcon.setImageResource(
                    when (position) {
                        0 -> R.drawable.soccer_ball
                        1 -> R.drawable.basketball_ball
                        2 -> R.drawable.tennis_ball
                        3 -> R.drawable.handball_ball
                        4 -> R.drawable.volleyball_ball
                        else -> R.drawable.soccer_ball
                    }
                )
            }
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }
}