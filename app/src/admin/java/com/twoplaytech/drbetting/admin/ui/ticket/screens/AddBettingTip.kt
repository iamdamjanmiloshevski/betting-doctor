package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.twoplaytech.drbetting.admin.ui.ticket.components.InputField
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TextInput
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.showDatePicker
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.util.beautify

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:06
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun AddBettingTip(
    activity:AppCompatActivity? = null,
    navController: NavController = NavController(LocalContext.current),
    ticketsViewModel: TicketsViewModel = hiltViewModel()
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                TextInput(title = "League Name") {
                    InputField(valueState = leagueName, labelId = "League Name")
                }
                TextInput(title = "Home Team") {
                    InputField(valueState = homeTeam, labelId = "Home Team")
                }
                TextInput(title = "Away team") {
                    InputField(valueState = awayTeam, labelId = "Away team")
                }
                TextInput(title = "Betting Tip") {
                    InputField(valueState = bettingTip, labelId = "Betting Tip")
                }
                TextInput(title = "Result") {
                    InputField(valueState = result, labelId = "Result")
                }
                TextInput(title = "Game time") {
                    InputField(valueState = gameTime, labelId = "Game time", enabled = false){
                        activity?.let {
                            showDatePicker(activity){
                                gameTime.value = it!!.beautify()
                            }
                        }
                    }
                }
            }
        }
    }
}