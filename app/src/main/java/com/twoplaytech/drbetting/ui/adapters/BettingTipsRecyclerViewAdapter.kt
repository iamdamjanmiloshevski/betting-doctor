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
import com.twoplaytech.drbetting.data.BettingTip
import com.twoplaytech.drbetting.databinding.ItemTipBinding
import com.twoplaytech.drbetting.ui.common.OnBettingTipClickedListener
import com.twoplaytech.drbetting.ui.viewholders.TipViewHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:20 PM

*/
class BettingTipsRecyclerViewAdapter(private val tips: MutableList<BettingTip>) :
    RecyclerView.Adapter<TipViewHolder>() {
    private var listener:OnBettingTipClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTipBinding.inflate(inflater, parent, false)
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val bettingTip = tips[position]
        holder.binding.bettingType = bettingTip
        holder.binding.root.setOnClickListener {
            listener?.onTipClick(bettingTip)
        }
        holder.binding.root.setOnLongClickListener {
            listener?.onTipLongClick(bettingTip)
            true
        }
    }

    override fun getItemCount(): Int {
        return tips.size
    }

    fun addData(tips: List<BettingTip>) {
        this.tips.clear()
        this.tips.addAll(tips)
        notifyDataSetChanged()
    }
    fun setOnBettingTipClickedListener(listener: OnBettingTipClickedListener){
        this.listener = listener
    }
    fun clear(){
        this.tips.clear()
        notifyDataSetChanged()
    }

}