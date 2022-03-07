package com.twoplaytech.drbetting.admin.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/*
    Author: Damjan Miloshevski 
    Created on 7.3.22 14:55
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
sealed interface Validator {
    val hasError: MutableState<Boolean>
    val message: String
}

data class FieldValidator(
    override val message: String,
    override val hasError: MutableState<Boolean> = mutableStateOf(false)
) : Validator