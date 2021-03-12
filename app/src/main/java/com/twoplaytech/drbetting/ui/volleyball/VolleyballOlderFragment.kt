package com.twoplaytech.drbetting.ui.volleyball

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
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@AndroidEntryPoint
class VolleyballOlderFragment:BaseChildFragment(),Observer<Resource<List<BettingType>>> {
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
        requestOlderData(Sport.VOLLEYBALL)
        viewModel.observeOnOlderTips().observe(viewLifecycleOwner, this)
    }
    companion object{
        fun getInstance():VolleyballOlderFragment{
            return VolleyballOlderFragment()
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