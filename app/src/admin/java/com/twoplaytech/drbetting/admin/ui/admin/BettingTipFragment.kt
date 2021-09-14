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
import androidx.fragment.app.activityViewModels
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.OnDropdownItemSelectedListener
import com.twoplaytech.drbetting.admin.ui.common.BaseFragment
import com.twoplaytech.drbetting.admin.ui.viewmodels.AdminViewModel
import com.twoplaytech.drbetting.admin.util.Constants
import com.twoplaytech.drbetting.admin.views.ChooserView
import com.twoplaytech.drbetting.data.models.*
import com.twoplaytech.drbetting.databinding.FragmentBettingTipBinding
import java.util.*

class BettingTipFragment : BaseFragment(), OnDropdownItemSelectedListener {

    private lateinit var _binding: FragmentBettingTipBinding
    private val adminViewModel: AdminViewModel by activityViewModels()
    private var bettingTip: BettingTip? = null
    private var cancel = false
    private var sportChosen = Sport.Football
    private var statusChosen = TypeStatus.UNKNOWN
    private var sportChosenIdx = 0
    private lateinit var menu: Menu

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
        }
        populateExistingTip(bettingTip)
        return _binding.root
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun populateExistingTip(bettingTip: BettingTip?) {
        bettingTip?.let {
            with(_binding) {
                this.tvLeague.setText(bettingTip.leagueName)
                this.tvHome.setText(bettingTip.teamHome?.name ?: "undefined")
                this.tvAway.setText(bettingTip.teamAway?.name ?: "undefined")
                this.tvBettingTip.setText(bettingTip.bettingType)
                this.tvResult.setText(bettingTip.result)
                bettingTip.gameTime?.let { date ->
                    this.tvGameTime.setDate(date)
                }
                bettingTip.sport.select(this.cvSports)
                bettingTip.status?.select(this.cvStatus)
            }
        } ?: statusChosen.select(_binding.cvStatus)
    }

    override fun observeData() {
        adminViewModel.observeValidation().observe(viewLifecycleOwner, { hasErrors ->
            when (hasErrors) {
                true -> {
                }
                false -> {
                    bettingTip?.let {
                        adminViewModel.updateBettingTip(getTipFromInput(it._id))
                    } ?: adminViewModel.insertBettingTip(getTipFromInput())
                }
            }
        })
        adminViewModel.observeForInsertedBettingTip().observe(viewLifecycleOwner, { resource ->
            val saveIcon = menu.findItem(R.id.action_save)
            saveIcon.isVisible = resource.status != Status.LOADING
        })
        adminViewModel.observeOnUpdatedTip().observe(viewLifecycleOwner, { resource ->
            val saveIcon = menu.findItem(R.id.action_save)
            saveIcon.isVisible = resource.status != Status.LOADING
        })
    }

    private fun getTipFromInput(id: String? = null): BettingTip {
        val league = _binding.tvLeague.getInput<String?>() as String
        val bettingTip = _binding.tvBettingTip.getInput<String?>() as String
        val gameTime = _binding.tvGameTime.getInput<Date?>()
        val result = _binding.tvResult.getInput<String?>() as String
        val teamHomeName = _binding.tvHome.getInput<String>()
        val teamAwayName = _binding.tvAway.getInput<String>()
        val teamHome =
            Team(teamHomeName ?: throw NullPointerException("Team name must not be null"))
        val teamAway =
            Team(teamAwayName ?: throw NullPointerException("Team name must not be null"))
        return BettingTip(
            _id = id,
            leagueName = league,
            teamHome = teamHome,
            teamAway = teamAway,
            gameTime = gameTime!!,
            bettingType = bettingTip,
            status = statusChosen,
            result = result,
            sport = sportChosen
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_betting_tip, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                adminViewModel.validate(validateFields())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(): Boolean {
        val league = _binding.tvLeague.getInput<String>()
        val bettingTip = _binding.tvBettingTip.getInput<String?>()
        val gameTime = _binding.tvGameTime.getInput<Date?>()
        val teamHome = _binding.tvHome.getInput<String?>()
        val teamAway = _binding.tvAway.getInput<String?>()
        val result = _binding.tvResult.getInput<String?>()
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
        sportChosen = sport
        sportChosenIdx = when (sport) {
            Sport.Football -> 0
            Sport.Basketball -> 1
            Sport.Tennis -> 2
            Sport.Handball -> 3
            Sport.Volleyball -> 4
        }
    }

    override fun onStatusClicked(status: TypeStatus) {
        statusChosen = status
    }

    private fun Sport.select(chooserView: ChooserView) {
        when (this) {
            Sport.Football -> chooserView.setSelection(0)
            Sport.Basketball -> chooserView.setSelection(1)
            Sport.Tennis -> chooserView.setSelection(2)
            Sport.Handball -> chooserView.setSelection(3)
            Sport.Volleyball -> chooserView.setSelection(4)
        }
    }

    private fun TypeStatus.select(chooserView: ChooserView) {
        when (this) {
            TypeStatus.UNKNOWN -> chooserView.setSelection(2)
            TypeStatus.WON -> chooserView.setSelection(0)
            TypeStatus.LOST -> chooserView.setSelection(1)
        }
    }
}