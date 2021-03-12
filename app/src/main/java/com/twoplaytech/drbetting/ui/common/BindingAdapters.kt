package com.twoplaytech.drbetting.ui.common

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.BettingType
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Team
import com.twoplaytech.drbetting.data.TypeStatus
import com.twoplaytech.drbetting.util.getSportPlaceHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 3:01 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@BindingAdapter("game")
fun setGameName(view: AppCompatTextView, bettingType: BettingType) {
    val game = "${bettingType.teamHome?.name} - ${bettingType.teamAway?.name}"
    view.text = game
}

@BindingAdapter("result")
fun setResult(view: AppCompatImageView, status: TypeStatus) {
    when (status) {
        TypeStatus.UNKNOWN -> view.setImageResource(R.drawable.tip_unknown)
        TypeStatus.WON -> view.setImageResource(R.drawable.tip_won)
        TypeStatus.LOST -> view.setImageResource(R.drawable.tip_lost)
    }
}

@BindingAdapter("team","sport")
fun loadTeamLogo(view:AppCompatImageView,team:Team,sport: Sport){
    Glide.with(view)
        .load(team.logo)
        .fitCenter()
        .error(sport.getSportPlaceHolder())
        .placeholder(R.drawable.progress_animation)
        .into(view)
}

@BindingAdapter("sportIcon")
fun getSportIcon(view: AppCompatImageView, tipSport: Sport) {
    when (tipSport) {
        Sport.FOOTBALL -> view.setImageResource(R.drawable.soccer_ball)
        Sport.BASKETBALL -> view.setImageResource(R.drawable.basketball_ball)
        Sport.TENNIS -> view.setImageResource(R.drawable.tennis_ball)
        Sport.HANDBALL -> view.setImageResource(R.drawable.handball_ball)
        Sport.VOLLEYBALL -> view.setImageResource(R.drawable.volleyball_ball)

    }
}