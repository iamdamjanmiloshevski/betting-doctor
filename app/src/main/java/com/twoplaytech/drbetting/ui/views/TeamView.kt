package com.twoplaytech.drbetting.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.data.models.Sport
import com.twoplaytech.drbetting.data.models.Team
import com.twoplaytech.drbetting.databinding.ViewTeamBinding
import com.twoplaytech.drbetting.ui.common.ICustomView

/*
    Author: Damjan Miloshevski 
    Created on 7.10.21 12:53
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class TeamView(context:Context,attrs:AttributeSet):ConstraintLayout(context,attrs),ICustomView {
    private lateinit var binding:ViewTeamBinding

    init {
        initBinding(LayoutInflater.from(context))
    }

    fun setTeamData(team:Team,sport:Sport){
        binding.team = team
        binding.sport = sport
        requestLayout()
        invalidate()
    }

    override fun initBinding(inflater: LayoutInflater) {
          binding = ViewTeamBinding.inflate(inflater,this,true)
    }
}