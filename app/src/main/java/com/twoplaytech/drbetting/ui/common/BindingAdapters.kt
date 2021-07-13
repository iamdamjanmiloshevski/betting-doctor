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

package com.twoplaytech.drbetting.ui.common

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.common.ISettingsItem
import com.twoplaytech.drbetting.data.BettingTip
import com.twoplaytech.drbetting.data.Sport
import com.twoplaytech.drbetting.data.Team
import com.twoplaytech.drbetting.data.TypeStatus
import com.twoplaytech.drbetting.glide.GlideApp
import com.twoplaytech.drbetting.glide.SvgSoftwareLayerSetter
import com.twoplaytech.drbetting.util.getSportPlaceHolder

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 3:01 PM

*/
@BindingAdapter("game")
fun setGameName(view: AppCompatTextView, bettingTip: BettingTip) {
    val game = "${bettingTip.teamHome?.name} - ${bettingTip.teamAway?.name}"
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

@BindingAdapter("team", "sport")
fun loadTeamLogo(view: AppCompatImageView, team: Team, sport: Sport) {

    if (team.logo.endsWith(".svg")) {
        val requestBuilder = GlideApp.with(view.context)
            .`as`(PictureDrawable::class.java)
            .listener(SvgSoftwareLayerSetter())
        requestBuilder.load(team.logo)
            .error(sport.getSportPlaceHolder())
            .into(view)
    } else {
        GlideApp.with(view.context)
            .load(team.logo)
            .fitCenter()
            .error(sport.getSportPlaceHolder())
            .placeholder(R.drawable.progress_animation)
            .into(view)
    }
}

@BindingAdapter("sportIcon")
fun getSportIcon(view: AppCompatImageView, tipSport: Sport) {
    when (tipSport) {
        Sport.Football -> view.setImageResource(R.drawable.soccer_ball)
        Sport.Basketball -> view.setImageResource(R.drawable.basketball_ball)
        Sport.Tennis -> view.setImageResource(R.drawable.tennis_ball)
        Sport.Handball -> view.setImageResource(R.drawable.handball_ball)
        Sport.Volleyball -> view.setImageResource(R.drawable.volleyball_ball)

    }
}

@BindingAdapter("itemIcon")
fun loadItemIcon(view: ImageView, item: ISettingsItem) {
    Glide.with(view)
        .load(item.icon)
        .fitCenter()
        .placeholder(R.drawable.progress_animation)
        .into(view)
}