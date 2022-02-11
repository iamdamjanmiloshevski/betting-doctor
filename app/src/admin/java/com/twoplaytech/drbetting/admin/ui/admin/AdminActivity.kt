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
import android.widget.*
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.BaseAdminActivity
import com.twoplaytech.drbetting.admin.ui.auth.LoginActivity
import com.twoplaytech.drbetting.admin.ui.viewmodels.BettingTipsViewModel
import com.twoplaytech.drbetting.admin.ui.viewmodels.LoginViewModel
import com.twoplaytech.drbetting.admin.util.Constants
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_ARGS
import com.twoplaytech.drbetting.admin.util.Constants.KEY_BETTING_TIP
import com.twoplaytech.drbetting.admin.util.Constants.KEY_TYPE
import com.twoplaytech.drbetting.admin.util.Constants.KEY_VIEW_TYPE
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_EDIT
import com.twoplaytech.drbetting.admin.util.SessionVerifierWorker
import com.twoplaytech.drbetting.data.*
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.Status
import com.twoplaytech.drbetting.databinding.ActivityAdminBinding
import com.twoplaytech.drbetting.ui.adapters.BettingTipsRecyclerViewAdapter
import com.twoplaytech.drbetting.ui.common.OnBettingTipClickedListener
import com.twoplaytech.drbetting.util.getSportColor
import com.twoplaytech.drbetting.util.getSportFromIndex
import timber.log.Timber

class AdminActivity : BaseAdminActivity(), AdapterView.OnItemSelectedListener,
    OnBettingTipClickedListener, PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    private lateinit var binding: ActivityAdminBinding
    private var typeSelected = 1
    private var sportSelected = 0
    private val viewModel: BettingTipsViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private var isExpanded = false

    private val bettingTips = mutableListOf<BettingTip>()
    private val adapter: BettingTipsRecyclerViewAdapter =
        BettingTipsRecyclerViewAdapter(bettingTips)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setContentView(binding.root)
        getExtras(intent)
        initUI()
    }

    override fun initUI() {
        binding.fab.setOnClickListener(this)
        binding.fabBettingTip.setOnClickListener(this)
        binding.fabTicket.setOnClickListener(this)
        binding.noDataView.setVisible(false)
        showFabs()
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

    private fun checkSession() {
        bettingTipsViewModel.observeAppLaunch().observe(this,{appLaunchesCount ->
            if (appLaunchesCount>0) {
                val sessionVerifierWorkRequest =
                    OneTimeWorkRequestBuilder<SessionVerifierWorker>().build()
                WorkManager.getInstance(this).enqueue(sessionVerifierWorkRequest)
                WorkManager.getInstance(this).getWorkInfoByIdLiveData(sessionVerifierWorkRequest.id)
                    .observe(this,
                        { workInfo ->
                            if(workInfo.state ==  WorkInfo.State.FAILED){
                                MaterialDialog(this).show {
                                    title(null, getString(R.string.session))
                                    message(R.string.session_expired)
                                    cancelable(false)
                                    positiveButton(android.R.string.ok,null) {
                                        dismiss()
                                        logout()
                                    }
                                }
                            }else if(workInfo.state == WorkInfo.State.SUCCEEDED){
                                observeData()
                            }
                        }
                    )
            } else {
                bettingTipsViewModel.incrementAppLaunch()
                observeData()
            }
        })
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
        viewModel.observeTips().observe(this, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val items = resource.data as List<BettingTip>
                    adapter.addData(items)
                }
                Status.ERROR -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
            }
        })
        adminViewModel.observeDeletedTip().observe(this,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        changeData(sportSelected.getSportFromIndex(), typeSelected)
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            })
        adminViewModel.observeNotifications().observe(this,{
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
    }

    override fun initBinding() {
        binding = ActivityAdminBinding.inflate(layoutInflater)
    }

    override fun getExtras(intent: Intent) {
        intent.extras?.let {
            typeSelected = preferencesManager.getInteger(KEY_VIEW_TYPE)
            sportSelected = it.getInt(KEY_SPORT)
            binding.spSport.setSelection(sportSelected)
        }
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
        val color = ContextCompat.getColor(this, sport.getSportColor())
        val fabs = listOf(binding.fab,binding.fabBettingTip,binding.fabTicket)
        for(fab in fabs){
            fab.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun changeData(sport: Sport, typeSelected: Int) {
        when (typeSelected) {
            0 -> requestOlderData(sport)
            1 -> requestTodayData(sport)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun requestTodayData(sport: Sport) {
        binding.noDataView.setVisible(false)
        binding.progressBar.visibility = View.VISIBLE
        binding.rvBettingTips.visibility = View.GONE
        viewModel.getBettingTips(sport, true)
    }


    fun requestOlderData(sport: Sport) {
        binding.noDataView.setVisible(false)
        binding.progressBar.visibility = View.VISIBLE
        binding.rvBettingTips.visibility = View.GONE
        viewModel.getBettingTips(sport, false)
    }

    override fun onTipClick(tip: BettingTip) {
        this.navigateToTips(VIEW_TYPE_EDIT, tip)
    }

    override fun onTipLongClick(tip: BettingTip) {
        MaterialDialog(this).show {
            cancelable(false)
            title(null, "Delete tip?")
            message(null, "Are you sure that you want to delete this tip?")
            positiveButton(android.R.string.ok, null) {
                tip._id?.let { id -> adminViewModel.deleteBettingTip(id) }
            }
            negativeButton(android.R.string.cancel, null) {
                dismiss()
            }
        }
    }

    private fun navigateToTips(viewType: Int, tip: BettingTip? = null) {
        preferencesManager.saveInteger(KEY_VIEW_TYPE, typeSelected)
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
                    positiveButton(android.R.string.ok, null) {
                        logout()
                    }
                    negativeButton(android.R.string.cancel, null) {
                        dismiss()
                    }
                }
                true
            }
            R.id.action_notifications -> {
                MaterialDialog(this).show {
                    cancelable(false)
                    title(null,"Send notification")
                    message(null,"Are you sure that you want to notify all the clients?")
                    positiveButton(android.R.string.ok, null) {
                        adminViewModel.sendNotification()
                    }
                    negativeButton(android.R.string.cancel, null) {
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

    private fun logout() {
        loginViewModel.logout()
        startActivity(Intent(this@AdminActivity, LoginActivity::class.java))
        finishAffinity()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_more -> showMenu(binding.ivMore)
            R.id.fab ->{
                Timber.e("Is expanded $isExpanded")
                isExpanded = !isExpanded
                showFabs()
            }
            R.id.fabBettingTip -> this.navigateToTips(Constants.VIEW_TYPE_NEW)
            R.id.fabTicket -> {}
        }
    }

    private fun showFabs() {
        if(!isExpanded){
            binding.fabBettingTip.visibility = View.GONE
            binding.fabTicket.visibility = View.GONE
        } else {
            binding.fabBettingTip.visibility = View.VISIBLE
            binding.fabTicket.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        bettingTipsViewModel.getAppLaunchCount()
        checkSession()
        when (typeSelected) {
            0 -> requestOlderData(sportSelected.getSportFromIndex())
            1 -> requestTodayData(sportSelected.getSportFromIndex())
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

    override fun onDestroy() {
        super.onDestroy()
        binding.rvBettingTips.adapter = null
    }

    companion object {
        const val KEY_SPORT = "KEY_SPORT"
    }

}