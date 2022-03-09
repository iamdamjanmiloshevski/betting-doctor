package com.twoplaytech.drbetting.admin.data.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.twoplaytech.drbetting.data.models.BettingTipInput

/*
    Author: Damjan Miloshevski 
    Created on 22.2.22 15:36
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Keep
data class TicketInput(
@SerializedName("date")
val date: String,
@SerializedName("_id")
val id: String? = null,
@SerializedName("tips")
val tips: List<BettingTipInput>
)

