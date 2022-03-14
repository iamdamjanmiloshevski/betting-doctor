package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.sportsanalyst.ui.components.SportsAnalystAppBar
import com.twoplaytech.drbetting.sportsanalyst.ui.screens.settings.SettingsViewModel
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SomeGreen

/*
    Author: Damjan Miloshevski 
    Created on 11.3.22 15:56
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun Settings(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val checkedState = remember { mutableStateOf(settingsViewModel.pushNotificationsState.value) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        SportsAnalystAppBar(
            title = stringResource(id = R.string.item_settings), true, onBackPressed = {
                navController.navigateUp()
            })
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            val pushNotificationsCategory = stringResource(id = R.string.push_notifications)
            SettingCategory(pushNotificationsCategory) {
                val settingTitle = stringResource(R.string.receive_string_notification)
                val enabledSettingMessage = stringResource(R.string.push_notifications_enabled)
                val disabledSettingMessage = stringResource(R.string.push_notifications_disabled)
                SettingSwitch(
                    settingTitle,
                    checkedState,
                    enabledSettingMessage,
                    disabledSettingMessage
                ) {
                    settingsViewModel.enableNotifications(it)
                }
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
    disabledSettingMessage: String,
    onSettingChanged: (Boolean) -> Unit
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
            onCheckedChange = {
                checkedState.value = it
                onSettingChanged.invoke(it)
            },
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