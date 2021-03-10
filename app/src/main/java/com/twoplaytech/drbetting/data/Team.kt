package com.twoplaytech.drbetting.data

import com.twoplaytech.drbetting.util.Constants.TEAM_LOGO
import com.twoplaytech.drbetting.util.Constants.TEAM_NAME
import com.twoplaytech.drbetting.util.checkImageExtension
import java.io.Serializable

/*
    Author: Damjan Miloshevski 
    Created on 3/10/21 12:26 PM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
data class Team(var name: String = "", var logo: String = "") : Serializable {
    constructor(data: Map<*, *>) : this() {
        name = if (data.containsKey(TEAM_NAME)
            && data[TEAM_NAME] != null) {
            data[TEAM_NAME] as String
        } else {
            ""
        }
        logo = if (data.containsKey(TEAM_LOGO)
            && data[TEAM_LOGO] != null) {
            if (!(data[TEAM_LOGO] as String).checkImageExtension()) {
                ""
            } else {
                data[TEAM_LOGO] as String
            }
        } else {
            ""
        }
    }
}
