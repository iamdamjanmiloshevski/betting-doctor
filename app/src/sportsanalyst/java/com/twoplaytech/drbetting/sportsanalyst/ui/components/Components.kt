package com.twoplaytech.drbetting.sportsanalyst.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Team
import com.twoplaytech.drbetting.sportsanalyst.data.Resource
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.Aldrich
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SilverChalice
import com.twoplaytech.drbetting.sportsanalyst.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.util.getSportPlaceHolder
import com.twoplaytech.drbetting.util.getStatusResource

/*
    Author: Damjan Miloshevski 
    Created on 11.2.22 09:29
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/



@Composable
fun TicketInfo(viewModel: TicketsViewModel) {
    Surface(modifier = Modifier.fillMaxSize(), color = SilverChalice) {
        when (val ticket = viewModel.ticket) {
            is Resource.Error -> {
                CenteredItem {
                    Text(text = ticket.message.toString())
                }
            }
            is Resource.Loading -> {
                CenteredItem {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                ticket.data?.let {
                    LazyColumn(contentPadding = PaddingValues(10.dp)) {
                        items(it.tips) { bettingTip ->
                            TipCard(bettingTip = bettingTip)
                        }
                    }
                }

            }
        }
    }
}