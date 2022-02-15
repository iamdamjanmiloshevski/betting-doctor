package com.twoplaytech.drbetting.sportsanalyst.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.twoplaytech.drbetting.sportsanalyst.ui.screens.Splash
import com.twoplaytech.drbetting.sportsanalyst.ui.screens.Ticket
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel

/*
    Author: Damjan Miloshevski 
    Created on 10.2.22 16:21
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun SportsAnalystNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.Splash.name) {
        composable(Route.Splash.name) { Splash(navController) }
        composable(Route.Ticket.name) {
            val viewModel = hiltViewModel<TicketsViewModel>()
            Ticket(navController, viewModel)
        }
    }
}