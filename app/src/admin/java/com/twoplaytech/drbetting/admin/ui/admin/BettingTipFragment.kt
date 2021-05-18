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
import androidx.fragment.app.Fragment
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.common.OnDropdownItemSelectedListener
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_EDIT
import com.twoplaytech.drbetting.admin.util.Constants.VIEW_TYPE_NEW
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.TypeStatus
import com.twoplaytech.drbetting.databinding.FragmentBettingTipBinding
import java.util.*

class BettingTipFragment : Fragment(), OnDropdownItemSelectedListener {

    private lateinit var binding: FragmentBettingTipBinding
    private var bettingTip: BettingType? = null
    private var cancel = false
    private var type = VIEW_TYPE_NEW

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBettingTipBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding.cvSports.setOnDropDownItemSelectedListener(this)
        binding.cvStatus.setOnDropDownItemSelectedListener(this)
        arguments?.let {
            bettingTip = it.getSerializable("betting_tip") as BettingType?
            type = it.getInt("type")
        }
        when(type){
            VIEW_TYPE_NEW -> activity?.title = "Add new betting tip"
            VIEW_TYPE_EDIT -> activity?.title = "Update betting tip"
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_betting_tip, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {

                val league = binding.tvLeague.getInput<String>()
                val bettingTip = binding.tvBettingTip.getInput<String?>()
                val gameTime = binding.tvGameTime.getInput<Date?>()
                val teamHome = binding.tvHome.getTeamName()
                val teamAway = binding.tvAway.getTeamName()
                cancel = if (teamHome.isNullOrEmpty()) {
                    binding.tvHome.setError("Please provide a home team name!")
                    true
                } else false
                cancel = if (teamAway.isNullOrEmpty()) {
                    binding.tvAway.setError("Please provide a home team name!")
                    true
                } else false
                cancel = if (league.isNullOrEmpty()) {
                    binding.tvLeague.setError("Please provide a League name!")
                    true
                } else false
                cancel = if (bettingTip.isNullOrEmpty()) {
                    binding.tvBettingTip.setError("Please provide a betting tip!")
                    true
                } else false
                cancel = if (gameTime == null) {
                    binding.tvGameTime.setError("Please provide a game time!")
                    true
                } else false
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSportClicked(sport: Sport) {

    }

    override fun onStatusClicked(status: TypeStatus) {

    }
}