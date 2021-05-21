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

package com.twoplaytech.drbetting.admin.ui.admin

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.login.LoginActivity
import com.twoplaytech.drbetting.admin.ui.login.LoginViewModel
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_ARGS
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_TIP
import com.twoplaytech.drbetting.admin.util.Constants.KEY_TYPE
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_EDIT
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_NEW
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Status
import com.twoplaytech.drbetting.databinding.ActivityAdminBinding
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter
import com.twoplaytech.drbetting.ui.common.BaseActivity
import com.twoplaytech.drbetting.ui.common.BettingTipsViewModel
import com.twoplaytech.drbetting.ui.common.OnBettingTipClickedListener
import com.twoplaytech.drbetting.util.getSportColor
import com.twoplaytech.drbetting.util.getSportFromIndex

class AdminActivity : BaseActivity(), AdapterView.OnItemSelectedListener,
    OnBettingTipClickedListener, PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    private lateinit var binding: ActivityAdminBinding
    private var typeSelected = 1
    private var sportSelected = 0
    private val viewModel: BettingTipsViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val bettingTips = mutableListOf<BettingType>()
    private val adapter: BettingTipsRecyclerViewAdapter =
        BettingTipsRecyclerViewAdapter(bettingTips)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setContentView(binding.root)
        initUI()
        observeData()
        binding.fab.setOnClickListener { view ->
            this.navigateToTips(VIEW_TYPE_NEW)
        }
    }

    override fun initUI() {
        binding.noDataView.setVisible(false)
        adapter.setOnBettingTipClickedListener(this)
        binding.rvBettingTips.adapter = adapter
        binding.rvBettingTips.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val isVisible = adapter.itemCount > 0
                binding.rvBettingTips.visibility = if (isVisible) View.VISIBLE else View.GONE
                binding.noDataView.setVisible(!isVisible)
                binding.progressBar.visibility = View.GONE
            }
        })
        binding.ivMore.setOnClickListener(this)
        initSpinners()
    }

    private fun initSpinners() {
        val spinnerTypes = binding.spType
        val spinnerSports = binding.spSport
        spinnerTypes.initTypesSpinner()
        spinnerSports.initSportsSpinner()
        spinnerTypes.setSelection(typeSelected)
        spinnerSports.setSelection(sportSelected)
        spinnerSports.onItemSelectedListener = this
        spinnerTypes.onItemSelectedListener = this
    }

    override fun observeData() {
        viewModel.observeOnOlderTips().observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val data = resource.data as List<BettingType>
                    adapter.addData(data)
                }
                Status.LOADING -> {
                    binding.noDataView.setVisible(false)
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {


                }
            }
        })
        loginViewModel.observeLogin().observe(this, Observer {
            when (it.status) {
                com.twoplaytech.drbetting.admin.common.Status.SUCCESS -> {
                    when (it.data) {
                        true -> {
                        }
                        false -> {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
                com.twoplaytech.drbetting.admin.common.Status.ERROR -> {
                    Snackbar.make(
                        binding.adminView,
                        it.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                com.twoplaytech.drbetting.admin.common.Status.LOADING -> {

                }
            }
        })
    }

    override fun initBinding() {
        binding = ActivityAdminBinding.inflate(layoutInflater)
    }


    private fun Spinner.initTypesSpinner() {
        ArrayAdapter.createFromResource(
            this@AdminActivity,
            R.array.types,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.adapter = adapter
        }
    }

    private fun Spinner.initSportsSpinner() {
        ArrayAdapter.createFromResource(
            this@AdminActivity,
            R.array.sports,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.adapter = adapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var sport = sportSelected.getSportFromIndex()
        when (parent?.id) {
            R.id.sp_sport -> {
                sport = position.getSportFromIndex()
                sportSelected = position
                changeTheme(appBarLayout = binding.appBar, sport = sport)
                changeFabColor(sport)
                changeData(sport, typeSelected)
            }
            R.id.sp_type -> {
                when (position) {
                    0 -> binding.fab.visibility = View.GONE
                    1 -> binding.fab.visibility = View.VISIBLE
                }
                typeSelected = position
                changeData(sport, typeSelected)
            }
        }
    }

    private fun changeFabColor(sport: Sport) {
        binding.fab.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, sport.getSportColor()))
    }

    private fun changeData(sport: Sport, typeSelected: Int) {
        when (typeSelected) {
            0 -> requestOlderData(sport)
            1 -> requestTodayData(sport)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun requestTodayData(type: Sport) {
        bettingTips.clear()
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
        bettingTips.clear()
        viewModel.getOlderTips(type.value)
    }

    override fun onTipClick(tip: BettingType) {
        this.navigateToTips(VIEW_TYPE_EDIT, tip)
    }

    private fun navigateToTips(viewType: Int, tip: BettingType? = null) {
        val intent = Intent(this, BettingTipActivity::class.java)
        val args = bundleOf(
            KEY_TYPE to viewType,
            KEY_BETTING_TIP to tip
        )
        intent.putExtra(KEY_BETTING_ARGS, args)
        startActivity(intent)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_logout -> {
                MaterialDialog(this).show {
                    cancelable(false)
                    title(R.string.logout_title)
                    message(R.string.log_out_msg)
                    positiveButton(android.R.string.ok,null) {
                        loginViewModel.logout()
                    }
                    negativeButton(android.R.string.cancel,null) {
                        dismiss()
                    }
                }
                true
            }
            R.id.action_settings -> {
                true
            }
            else -> false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_more -> showMenu(binding.ivMore)
        }
    }

    private fun showMenu(anchor: View?) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_admin, popup.menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }
}