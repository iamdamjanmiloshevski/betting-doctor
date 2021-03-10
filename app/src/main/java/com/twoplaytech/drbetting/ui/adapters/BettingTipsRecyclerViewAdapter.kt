package com.twoplaytech.drbetting.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoplaytech.drbetting.databinding.TipItemBinding
import com.twoplaytech.drbetting.ui.basketball.BettingType
import com.twoplaytech.drbetting.ui.viewholders.TipViewHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 1:20 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class BettingTipsRecyclerViewAdapter(val tips:List<BettingType>):RecyclerView.Adapter<TipViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TipItemBinding.inflate(inflater,parent,false)
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = tips[position]
        holder.binding.type = tip
    }

    override fun getItemCount(): Int {
        return tips.size
    }
}