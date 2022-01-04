package com.twoplaytech.drbetting.sportsanalyst.data.models


import com.google.gson.annotations.SerializedName
import com.twoplaytech.drbetting.data.models.BettingTip

data class Ticket(
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("tips")
    val tips: List<BettingTip>
)