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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.ticket.components.BettingTipForm
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:06
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun AddBettingTip(
    activity: AppCompatActivity? = null,
    navController: NavController = NavController(LocalContext.current)
) {
    Scaffold(topBar = {
        TicketsAppBar(title = "Add new Betting tip", navController = navController, actions = {
            MenuAction(
                icon = Icons.Default.Check,
                contentDescription = "Save icon"
            ) { //todo save tip
            }
        })
    }) {
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
                chosenStatusIcon,
                onOpenCloseStatusSpinner
            )
        }
    }
}
