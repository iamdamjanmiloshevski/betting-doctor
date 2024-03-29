package com.twoplaytech.drbetting.admin.ui.ticket.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.twoplaytech.drbetting.admin.ui.ticket.screens.AddBettingTip
import com.twoplaytech.drbetting.admin.ui.ticket.screens.AddOrUpdateTicket
import com.twoplaytech.drbetting.admin.ui.ticket.screens.Tickets
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:01
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun TicketNavigation(activity: AppCompatActivity? = null) {
    val navController = rememberNavController()
    val ticketsViewModel = hiltViewModel<TicketsViewModel>()
    NavHost(navController, startDestination = TicketRoute.Tickets.name) {
        composable(TicketRoute.route(TicketRoute.Tickets)) {
            activity?.let {
                Tickets(activity, navController, ticketsViewModel)
            }
        }
        composable(TicketRoute.route(TicketRoute.AddOrUpdateTicket)){
            AddOrUpdateTicket(navController, ticketsViewModel = ticketsViewModel)
        }
        composable(
            TicketRoute.route(TicketRoute.AddOrUpdateBettingTip).plus("?ticketId={ticketId},?tipId={tipId}"),
            arguments = listOf(navArgument("ticketId") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            },navArgument("tipId") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            arguments?.let { args ->
                val ticketId = args["ticketId"] as String?
                val tipId = args["tipId"] as String?
                AddBettingTip(ticketId,tipId, activity, navController,ticketsViewModel)
            } ?: throw Exception(
                "Please provide arguments for destination ${
                    TicketRoute.route(
                        TicketRoute.AddOrUpdateBettingTip
                    )
                }"
            )
        }
    }
}