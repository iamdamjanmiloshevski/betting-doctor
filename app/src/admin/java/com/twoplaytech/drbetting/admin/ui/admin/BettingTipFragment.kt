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

import android.os.Bundle
import android.view.*
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.OnDropdownItemSelectedListener
import com.twoplaytech.drbetting.admin.util.Constants
import com.twoplaytech.drbetting.admin.util.Constants.KEY_TYPE
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_EDIT
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_NEW
import com.twoplaytech.drbetting.admin.views.ChooserView
import com.twoplaytech.drbetting.data.*
import com.twoplaytech.drbetting.databinding.FragmentBettingTipBinding
import com.twoplaytech.drbetting.ui.common.BaseFragment
import java.util.*

class BettingTipFragment : BaseFragment(), OnDropdownItemSelectedListener {

    private lateinit var _binding: FragmentBettingTipBinding
    private var bettingTip: BettingType? = null
    private var cancel = false
    private var sportChosen = Sport.FOOTBALL
    private var statusChosen = TypeStatus.UNKNOWN
    private var type = VIEW_TYPE_NEW

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBettingTipBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        _binding.cvSports.setOnDropDownItemSelectedListener(this)
        _binding.cvStatus.setOnDropDownItemSelectedListener(this)
        arguments?.let {
            bettingTip = it.getParcelable(Constants.KEY_BETTING_TIP)
            type = it.getInt(KEY_TYPE)
        }
        when (type) {
            VIEW_TYPE_NEW -> activity?.title = getString(R.string.new_betting_tip_title)
            VIEW_TYPE_EDIT -> activity?.title = getString(R.string.update_betting_tip_title)
        }
        populateExistingTip(bettingTip)
        return _binding.root
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun populateExistingTip(bettingTip: BettingType?) {
        bettingTip?.let {
            with(_binding) {
                this.tvLeague.setText(bettingTip.leagueName)
                this.tvHome.populate(bettingTip.teamHome)
                this.tvAway.populate(bettingTip.teamAway)
                this.tvBettingTip.setText(bettingTip.bettingType)
                this.tvResult.setText(bettingTip.result)
                bettingTip.gameTime?.let { date ->
                    this.tvGameTime.setDate(date)
                }
                bettingTip.sport?.select(this.cvSports)
                bettingTip.bettingType.select(this.cvStatus)
            }
        } ?: return
    }

    override fun observeData() {
        viewModel.observeValidation().observe(viewLifecycleOwner, { hasErrors ->
            when (hasErrors) {
                true -> {

                }
                false -> {
                    if (bettingTip != null) {
                        bettingTip?.let {
                            viewModel.saveBettingTip(it,true)
                        }
                    } else viewModel.saveBettingTip(getTipFromInput())
                }
            }
        })
        viewModel.observeForSavedTip().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    activity?.finish()
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {

                }
            }
        })
    }

    private fun getTipFromInput(): BettingType {
        val league = _binding.tvLeague.getInput<String?>() as String
        val bettingTip = _binding.tvBettingTip.getInput<String?>() as String
        val gameTime = _binding.tvGameTime.getInput<Date?>()
        val result = _binding.tvResult.getInput<String?>() as String
        val teamHomeName = _binding.tvHome.getTeamName()
        val teamAwayName = _binding.tvAway.getTeamName()
        val teamHome = Team(teamHomeName)
        val teamAway = Team(teamAwayName)
        return BettingType(
            league,
            teamHome,
            teamAway,
            gameTime,
            bettingTip,
            statusChosen,
            result,
            sport = sportChosen
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_betting_tip, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                viewModel.validate(validateFields())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(): Boolean {
        val league = _binding.tvLeague.getInput<String>()
        val bettingTip = _binding.tvBettingTip.getInput<String?>()
        val gameTime = _binding.tvGameTime.getInput<Date?>()
        val teamHome = _binding.tvHome.getTeamName()
        val teamAway = _binding.tvAway.getTeamName()
        val result = _binding.tvResult.getInput<String?>() as String
        cancel = if (teamHome.isNullOrEmpty()) {
            _binding.tvHome.setError(R.string.team_home_error)
            true
        } else false
        cancel = if (teamAway.isNullOrEmpty()) {
            _binding.tvAway.setError(R.string.team_away_error)
            true
        } else false
        cancel = if (league.isNullOrEmpty()) {
            _binding.tvLeague.setError(R.string.league_name_error)
            true
        } else false
        cancel = if (bettingTip.isNullOrEmpty()) {
            _binding.tvBettingTip.setError(R.string.betting_tip_error)
            true
        } else false
        cancel = if (result.isNullOrEmpty()) {
            _binding.tvBettingTip.setError(R.string.result_error)
            true
        } else false
        cancel = if (gameTime == null) {
            _binding.tvGameTime.setError(R.string.game_time_error)
            true
        } else false
        return cancel
    }


    override fun onSportClicked(sport: Sport) {
        val bettingTipActivity = activity as BettingTipActivity
        bettingTipActivity.changeThemePerSport(sport = sport)
        if (bettingTip != null) bettingTip?.sport = sport else sportChosen = sport
    }

    override fun onStatusClicked(status: TypeStatus) {
        if (bettingTip != null) bettingTip?.status = status else statusChosen = status
    }

    private fun Sport.select(chooserView: ChooserView) {
        when (this) {
            Sport.FOOTBALL -> chooserView.setSelection(0)
            Sport.BASKETBALL -> chooserView.setSelection(2)
            Sport.TENNIS -> chooserView.setSelection(3)
            Sport.HANDBALL -> chooserView.setSelection(4)
            Sport.VOLLEYBALL -> chooserView.setSelection(5)
        }
    }

    private fun String.select(chooserView: ChooserView) {
        when (this) {
            "0" -> chooserView.setSelection(3)
            "1" -> chooserView.setSelection(1)
            "2" -> chooserView.setSelection(2)
        }
    }
}