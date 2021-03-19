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

package com.twoplaytech.drbetting.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.databinding.FragmentChildBinding
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:35 PM

*/
abstract class BaseChildFragment : Fragment(), IBaseView {
    protected lateinit var binding: FragmentChildBinding
    override val viewModel: BettingTipsViewModel by activityViewModels()
    private val bettingTips = mutableListOf<BettingType>()
    protected val adapter: BettingTipsRecyclerViewAdapter =
        BettingTipsRecyclerViewAdapter(bettingTips)
    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentChildBinding.inflate(inflater, container, false)
    }
    override fun setUpDataAdapter() {
        binding.rvBettingTips.adapter = adapter
        binding.rvBettingTips.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val isVisible = adapter.itemCount > 0
                binding.rvBettingTips.visibility = if (isVisible) View.VISIBLE else View.GONE
                binding.noDataView.setVisible(!isVisible)
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    fun requestTodayData(type: Sport) {
        viewModel.getUpcomingTips(type.value).observe(this, {
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
            adapter.notifyDataSetChanged()
        })
    }

    fun requestOlderData(type: Sport) {
        viewModel.getOlderTips(type.value)
    }
}