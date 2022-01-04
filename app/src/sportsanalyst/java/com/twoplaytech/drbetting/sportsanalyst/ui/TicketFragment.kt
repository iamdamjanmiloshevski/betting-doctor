package com.twoplaytech.drbetting.sportsanalyst.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twoplaytech.drbetting.databinding.FragmentTicketBinding
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.twoplaytech.drbetting.data.models.Status as Status1

@AndroidEntryPoint
class TicketFragment : Fragment() {
    private lateinit var binding: FragmentTicketBinding
    private val viewModel: TicketsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater,container)
        observeData()
        return binding.root
    }

    private fun observeData() {
        viewModel.observeTicket().observe(viewLifecycleOwner,{resource->
            when(resource.status){
                Status1.SUCCESS -> {
                    val data = resource.data
                    Timber.e("$data")
                }
                Status1.ERROR -> {
                    Timber.e(resource.message)
                }
                Status1.LOADING -> {

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTicketByDate("27-12-2021")
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentTicketBinding.inflate(inflater,container,false)
    }

}