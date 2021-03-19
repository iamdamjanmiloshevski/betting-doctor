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

package com.twoplaytech.drbetting.ui.basketball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Resource
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Status
import com.twoplaytech.drbetting.ui.common.BaseChildFragment
import dagger.hilt.android.AndroidEntryPoint

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:37 PM

*/
@AndroidEntryPoint
class BasketballOlderFragment:BaseChildFragment(),Observer<Resource<List<BettingType>>> {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        initBinding(inflater, container)
        initUI()
        return binding.root
    }
    override fun initUI() {
        binding.noDataView.setVisible(false)
        setUpDataAdapter()
        requestOlderData(Sport.BASKETBALL)
        viewModel.observeOnOlderTips().observe(viewLifecycleOwner, this)
    }
    companion object{
        fun getInstance():BasketballOlderFragment{
            return BasketballOlderFragment()
        }
    }



    override fun onChanged(resource: Resource<List<BettingType>>) {
        when(resource.status){
            Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                val data = resource.data as List<BettingType>
                adapter.addData(data)
            }
            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            Status.ERROR -> {


            }
        }
    }
}