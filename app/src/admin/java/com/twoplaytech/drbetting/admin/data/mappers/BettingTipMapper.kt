package com.twoplaytech.drbetting.admin.data.mappers

import androidx.annotation.Keep
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.BettingTipInput

/*
    Author: Damjan Miloshevski 
    Created on 21.9.21 15:48
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@Keep
object BettingTipMapper {
    fun toBettingTipInput(bettingTip: BettingTip): BettingTipInput {
        return with(bettingTip) {
            BettingTipInput(
                this.leagueName,
                this.teamHome,
                this.teamAway,
                gameTime = this.gameTime,
                this.bettingType,
                status = this.status.name,
                this.result,
                this._id,
                sport = this.sport.name,
                coefficient,
                ticketId
            )
        }
    }
}