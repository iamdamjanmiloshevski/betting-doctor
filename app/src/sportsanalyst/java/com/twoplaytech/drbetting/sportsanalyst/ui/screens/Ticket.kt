package com.twoplaytech.drbetting.sportsanalyst.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.data.models.Ticket
import com.twoplaytech.drbetting.sportsanalyst.ui.components.TipCard
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SilverChalice
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.sportsanalyst.util.format
import com.twoplaytech.drbetting.sportsanalyst.util.today
import java.util.*

/*
    Author: Damjan Miloshevski 
    Created on 10.2.22 16:19
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun Ticket(
    navController: NavController = NavController(LocalContext.current),
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val date = Date(System.currentTimeMillis())
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(backgroundColor = SeaGreen) {
            Text(
                text = "Ticket ${date.format()}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }) {
        Surface(modifier = Modifier.fillMaxSize(), color = SilverChalice) {
            val ticket = produceState<Resource<Ticket>>(initialValue = Resource.Loading()) {
                value = viewModel.getTicketByDate1(today())
            }.value
            if (ticket.data == null) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            } else {
               LazyColumn(contentPadding = PaddingValues(10.dp)){
                  items(ticket.data.tips){bettingTip ->
                      TipCard(bettingTip = bettingTip)
                  }
               }
            }
        }
    }
}