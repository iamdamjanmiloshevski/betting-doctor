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

package com.twoplaytech.drbetting.ui.football

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twoplaytech.drbetting.data.BettingTip2
import com.twoplaytech.drbetting.data.Sport2
import com.twoplaytech.drbetting.data.Status
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter2
import com.twoplaytech.drbetting.ui.common.BaseChildFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:36 PM

*/
@AndroidEntryPoint
class FootballUpcomingFragment : BaseChildFragment() {
    private val tipsAdapter = BettingTipsRecyclerViewAdapter2(mutableListOf())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater, container)
        initUI()
        return binding.root
    }

    override fun initUI() {
        binding.noDataView.setVisible(false)
        //setUpDataAdapter()
        binding.rvBettingTips.adapter = tipsAdapter
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
        //requestTodayData(Sport.FOOTBALL)
        viewModel.getBettingTips(Sport2.FOOTBALL,true)
        viewModel.observeTips().observe(viewLifecycleOwner, { resource->
            when(resource.status){
                Status.SUCCESS ->{
                    val items = resource.data as List<BettingTip2>
                   tipsAdapter.addData(items)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
            }
        })
    }

    companion object {
        fun getInstance(): FootballUpcomingFragment {
            return FootballUpcomingFragment()
        }
    }
}