package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.admin.ui.ticket.components.InputField
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TextInput
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.DropDownList
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.showDatePicker
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
            mutableStateOf("Unknown".uppercase())
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val text = remember { mutableStateOf("") } // initial value
            val isOpenSpinnerSport = remember { mutableStateOf(false) } // initial value
            val onOpenCloseSportSpinner: (Boolean) -> Unit = {
                isOpenSpinnerSport.value = it
            }
            val isOpenSpinnerStatus = remember { mutableStateOf(false) } // initial value
            val onOpenCloseStatusSpinner: (Boolean) -> Unit = {
                isOpenSpinnerStatus.value = it
            }
            val userSelectedString: (String) -> Unit = {
                text.value = it
            }
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
                    InputField(valueState = gameTime, labelId = "Game time", enabled = false) {
                        activity?.let {
                            showDatePicker(activity) {
                                gameTime.value = it!!.beautify()
                            }
                        }
                    }
                    DropDownSpinner(
                        "Sport",
                        listOf(
                        "Football",
                        "Basketball",
                        "Tennis",
                        "Handball",
                        "Volleyball"
                    ),isOpenSpinnerSport, sport, onOpenCloseSportSpinner)
                    DropDownSpinner(
                        "Status",
                        listOf(
                            "Unknown".uppercase(),
                            "Won".uppercase(),
                            "Lost".uppercase()
                        ),isOpenSpinnerStatus, status, onOpenCloseStatusSpinner)
                }

            }
        }
    }
}

@Composable
private fun DropDownSpinner(
    title:String,
    items:List<String>,
    isOpen: MutableState<Boolean>,
    sport: MutableState<String>,
    openCloseOfDropDownList: (Boolean) -> Unit
) {
    Surface(modifier = Modifier
        .padding(
            top = 10.dp,
            bottom = 10.dp
        )
        .clickable { isOpen.value = true }) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = title, modifier = Modifier.padding(bottom = 5.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.5.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sport.value,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Icon(
                        imageVector = if (isOpen.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow dropdown"
                    )
                }
            }
            DropDownList(
                requestToOpen = isOpen.value, list = items, openCloseOfDropDownList
            ) {
                sport.value = it

            }
        }
    }
}