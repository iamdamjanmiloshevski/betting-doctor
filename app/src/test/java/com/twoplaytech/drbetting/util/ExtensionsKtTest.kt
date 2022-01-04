package com.twoplaytech.drbetting.util

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 22.11.21 14:54
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@RunWith(JUnit4::class)
class ExtensionsKtTest{

    @Test
    fun isOlderThan2Weeks_returnsTrue() {
        val dateToCheck = Calendar.getInstance()
        dateToCheck.add(Calendar.MONTH,11)
        dateToCheck.add(Calendar.DAY_OF_MONTH,7)
        dateToCheck.add(Calendar.YEAR,2021)

       // Assert.assertEquals(true,dateToCheck.timeInMillis.isOlderThan2Weeks())
    }
}