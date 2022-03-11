package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SomeGreen

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 15:56
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun Settings() {

    val checkedState = remember { mutableStateOf(true) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                text = "Settings"
            )
        }, navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back icon")
        }, backgroundColor = SeaGreen)
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            val pushNotificationsCategory = "Push notifications"
            SettingCategory(pushNotificationsCategory) {
                val settingTitle = "Receive push notifications"
                val enabledSettingMessage = "Push notifications are enabled"
                val disabledSettingMessage = "Push notifications are disabled"
                SettingSwitch(
                    settingTitle,
                    checkedState,
                    enabledSettingMessage,
                    disabledSettingMessage
                )
            }
        }
    }
}

@Composable
private fun SettingCategory(
    category: String,
    content: @Composable () -> Unit
) {
    Text(text = category, fontWeight = FontWeight.Bold)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        content()
        Divider(
            Modifier.height(20.dp),
            color = Color.White
        )
    }
}

@Composable
private fun SettingSwitch(
    settingTitle: String,
    checkedState: MutableState<Boolean>,
    enabledSettingMessage: String,
    disabledSettingMessage: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = settingTitle,
            color = Color.DarkGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = SeaGreen,
                checkedTrackColor = SomeGreen,
                uncheckedThumbColor = Color.Gray
            )
        )
    }
    Text(
        text = if (checkedState.value) enabledSettingMessage else disabledSettingMessage,
        fontWeight = FontWeight.Light
    )
}