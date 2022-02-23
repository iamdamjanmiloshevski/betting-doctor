package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.ticket.components.BettingTipForm
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport.Companion.toSport
import com.twoplaytech.drbetting.data.models.Team
import com.twoplaytech.drbetting.util.toStatus

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:06
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun AddBettingTip(
    ticketId:String? = null,
    activity: AppCompatActivity? = null,
    navController: NavController = NavController(LocalContext.current),
    viewModel:TicketsViewModel
) {
    val leagueName = rememberSaveable() {
        mutableStateOf("")
    }
    val homeTeam = rememberSaveable() {
        mutableStateOf("")
    }
    val awayTeam = rememberSaveable() {
        mutableStateOf("")
    }
    val bettingTip = rememberSaveable() {
        mutableStateOf("")
    }
    val result = rememberSaveable() {
        mutableStateOf("")
    }
    val gameTime = rememberSaveable() {
        mutableStateOf("")
    }
    val sport = rememberSaveable() {
        mutableStateOf("Football")
    }
    val status = rememberSaveable() {
        mutableStateOf("Unknown")
    }
    val coefficient = rememberSaveable() {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        TicketsAppBar(title = "Add new Betting tip", navController = navController, actions = {
            MenuAction(
                icon = Icons.Default.Check,
                contentDescription = "Save icon"
            ) {
                val bettingTip = BettingTip(
                    leagueName.value,
                    Team(homeTeam.value),
                    Team(awayTeam.value),
                    gameTime.value,
                    bettingTip.value,
                    status.value.uppercase().toStatus(),
                    result.value,
                    sport = sport.value.toSport(),
                    ticketId = ticketId,
                    coefficient = coefficient.value
                    )
                viewModel.bettingTips.add(bettingTip)
                navController.navigateUp()
            }
        })
    }) {
        val chosenSportIcon = rememberSaveable {
            mutableStateOf(R.drawable.soccer_ball)
        }
        val chosenStatusIcon = rememberSaveable {
            mutableStateOf(R.drawable.tip_unknown)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val isOpenSpinnerSport = remember { mutableStateOf(false) } // initial value
            val onOpenCloseSportSpinner: (Boolean) -> Unit = {
                isOpenSpinnerSport.value = it
            }
            val isOpenSpinnerStatus = remember { mutableStateOf(false) } // initial value
            val onOpenCloseStatusSpinner: (Boolean) -> Unit = {
                isOpenSpinnerStatus.value = it
            }
            BettingTipForm(
                leagueName,
                homeTeam,
                awayTeam,
                bettingTip,
                result,
                gameTime,
                activity,
                isOpenSpinnerSport,
                sport,
                chosenSportIcon,
                onOpenCloseSportSpinner,
                isOpenSpinnerStatus,
                status,
                coefficient,
                chosenStatusIcon,
                onOpenCloseStatusSpinner
            )
        }
    }
}