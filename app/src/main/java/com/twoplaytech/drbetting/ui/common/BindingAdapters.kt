/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

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