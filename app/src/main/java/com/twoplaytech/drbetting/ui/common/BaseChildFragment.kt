package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.databinding.FragmentChildBinding
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter
import com.twoplaytech.drbetting.ui.basketball.BettingType

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:35 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
abstract class BaseChildFragment : Fragment(), IBaseView {
    protected lateinit var binding: FragmentChildBinding
    override val viewModel: BettingTipsViewModel by activityViewModels()
    private val bettingTips = mutableListOf<BettingType>()
    private val adapter: BettingTipsRecyclerViewAdapter =
        BettingTipsRecyclerViewAdapter(bettingTips)

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentChildBinding.inflate(inflater, container, false)
    }

    override fun setUpDataAdapter() {
        binding.rvBettingTips.adapter = adapter
        binding.rvBettingTips.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun requestTodayData(type: Sport) {
        viewModel.getTodayTips(type.value).observe(this, {
            for (doc in it!!.documentChanges) {
                val tip = BettingType(doc.document.data)
                tip.id = doc.document.id
                when (doc.type) {
                    DocumentChange.Type.ADDED -> {
                        if (!bettingTips.contains(tip)) {
                            bettingTips.add(tip)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        for (i in bettingTips.indices) {
                            val footballTip = bettingTips[i]
                            if (footballTip?.id == tip.id) {
                                bettingTips[i] = tip
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                    DocumentChange.Type.REMOVED -> {
                        val iterator = bettingTips.iterator()
                        while (iterator.hasNext()) {
                            val bettingType = iterator.next()
                            if (bettingType == tip) {
                                iterator.remove()
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            if (bettingTips.isNotEmpty()) {
                adapter.notifyDataSetChanged()
            }
        })
    }
}