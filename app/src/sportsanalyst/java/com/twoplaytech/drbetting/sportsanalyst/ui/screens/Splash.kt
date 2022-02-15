package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.twoplaytech.drbetting.sportsanalyst.ui.navigation.Route
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/*
    Author: Damjan Miloshevski 
    Created on 10.2.22 16:21
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun Splash(navController: NavController = NavController(LocalContext.current)) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(key1 = true) {
            delay(TimeUnit.SECONDS.toMillis(2))
            navController.navigate(Route.Ticket.name){
                popUpTo(Route.Splash.name){inclusive = true}
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //todo finish screen
            Text(text = "Splash")
        }


    }
}