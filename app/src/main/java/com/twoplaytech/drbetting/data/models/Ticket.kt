package com.twoplaytech.drbetting.data.models


import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("date")
    val date: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("tips")
    val tips: List<BettingTip>
)