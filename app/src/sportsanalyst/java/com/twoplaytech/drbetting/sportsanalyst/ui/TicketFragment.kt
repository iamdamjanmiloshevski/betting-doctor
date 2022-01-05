package com.twoplaytech.drbetting.sportsanalyst.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.mappers.MessageMapper
import com.twoplaytech.drbetting.databinding.FragmentTicketBinding
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.sportsanalyst.util.beautifyDate
import com.twoplaytech.drbetting.sportsanalyst.util.toServerFormatDate
import com.twoplaytech.drbetting.sportsanalyst.util.today
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import com.twoplaytech.drbetting.data.models.Status as Status1

@AndroidEntryPoint
class TicketFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private lateinit var binding: FragmentTicketBinding
    private val viewModel: TicketsViewModel by viewModels()
    private var currentDate:Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        binding.toolbar.title = "Ticket"
        binding.toolbar.navigationIcon = null
        binding.toolbar.inflateMenu(R.menu.menu_ticket)
        binding.toolbar.setOnMenuItemClickListener(this)
        observeData()
        return binding.root
    }

    private fun observeData() {
        viewModel.observeTicket().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status1.SUCCESS -> {
                    val ticket = resource.data
                    ticket?.let {
                        binding.noData.visibility = View.GONE
                        binding.rvTips.visibility = View.VISIBLE
                        binding.toolbar.subtitle = it.date.beautifyDate()
                        binding.rvTips.apply {
                            this.adapter = BettingTipsRecyclerViewAdapter(it.tips.toMutableList())
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }

                }
                Status1.ERROR -> {
                    resource.message?.let { messageJson ->
                        val errorMessage = MessageMapper.fromJson(messageJson)
                        binding.noData.visibility = View.VISIBLE
                        binding.rvTips.visibility = View.GONE
                        binding.noData.text = errorMessage.message
                        binding.toolbar.subtitle = errorMessage.date.beautifyDate()
                    }
                }
                Status1.LOADING -> {

                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel.getTicketByDate(today())
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentTicketBinding.inflate(inflater, container, false)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.calendar -> {
                MaterialDialog(requireContext())
                    .cancelable(false)
                    .datePicker(currentDate = currentDate) { dialog, datetime ->
                        currentDate = datetime
                        viewModel.getTicketByDate(datetime.toServerFormatDate())
                        dialog.dismiss()
                    }.show()
            }
        }
        return true
    }
}