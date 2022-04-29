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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Status
import com.twoplaytech.drbetting.databinding.FragmentChildBinding
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter
import com.twoplaytech.drbetting.ui.states.BettingTipsUiState
import com.twoplaytech.drbetting.ui.viewmodels.BettingTipsViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:35 PM

*/
abstract class BaseChildFragment : Fragment(), BaseChildView, UiInitializer, BindingInitializer {
    protected lateinit var binding: FragmentChildBinding
    protected val viewModel: BettingTipsViewModel by activityViewModels()
    private val bettingTips = mutableListOf<BettingTip>()
    protected val adapter: BettingTipsRecyclerViewAdapter =
        BettingTipsRecyclerViewAdapter(bettingTips)

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentChildBinding.inflate(inflater, container, false)
    }

    override fun setUpDataAdapter() {
        binding.noDataView.setVisible(false)
        binding.rvBettingTips.adapter = adapter
        binding.rvBettingTips.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val isVisible = adapter.itemCount > 0
                binding.rvBettingTips.visibility = if (isVisible) View.VISIBLE else View.GONE
                binding.noDataView.setVisible(!isVisible)
            }
        })
    }

    override fun showLoader(show: Boolean) {
        if (show) binding.progressBar.visibility = View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bettingTipsState.collect { uiState ->
                    when (uiState) {
                        is BettingTipsUiState.Error -> {
                            showLoader(false)
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        BettingTipsUiState.Loading -> showLoader(true)
                        is BettingTipsUiState.Success -> {
                            showLoader(false)
                            binding.progressBar.visibility = View.GONE
                            val tips = uiState.tips
                            adapter.addData(tips)
                        }
                    }
                }
            }
        }
    }
}