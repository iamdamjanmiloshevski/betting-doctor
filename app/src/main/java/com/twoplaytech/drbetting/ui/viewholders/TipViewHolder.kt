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

package com.twoplaytech.drbetting.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.databinding.ItemTipBinding
import com.twoplaytech.drbetting.util.beautifyDate

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:20 PM

*/
class TipViewHolder(val binding: ItemTipBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(bettingTip: BettingTip, showSportInItem: Boolean = false) {
        with(bettingTip) {
            this.teamHome?.let {  binding.teamHome.setTeamData(it, this.sport) }
            this.teamAway?.let { binding.teamAway.setTeamData(it, this.sport) }
            binding.bettingInfo.setData(text = this.bettingType)
            binding.status.setData(status = this.status)
            binding.sport.setData(sport = bettingTip.sport)
            if(showSportInItem) binding.sport.visibility = View.INVISIBLE else View.VISIBLE
            binding.gameTime.text = this.gameTime.beautifyDate(binding.root.context)
        }
    }
}