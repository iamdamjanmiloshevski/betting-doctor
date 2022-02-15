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
import com.twoplaytech.drbetting.util.getSportPlaceHolder
import com.twoplaytech.drbetting.util.getStatusResource

/*
    Author: Damjan Miloshevski 
    Created on 11.2.22 09:29
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun TipCard(modifier: Modifier = Modifier, bettingTip: BettingTip? = null) {
    bettingTip?.let {
        Card(
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White,
            elevation = 5.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp, end = 5.dp, bottom = 0.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = it.leagueName, fontFamily = Aldrich)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TeamInfo(team = it.teamHome)
                    Text(
                        text = it.result,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Aldrich
                    )
                    TeamInfo(team = it.teamAway)
                }
                Spacer(modifier = Modifier.height(10.dp))
                BettingInfo(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp,
                            top = 0.dp
                        )
                        .fillMaxWidth(),
                    it
                )
            }
        }
    }
}

@Preview
@Composable
fun BettingInfo(modifier: Modifier = Modifier, bettingTip: BettingTip? = null) {
    bettingTip?.let {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BettingCard(
                title = stringResource(R.string.betting_tip),
                infoLabel = it.bettingType
            )
            BettingCard(
                title = stringResource(R.string.sport),
                icon = bettingTip.sport.getSportPlaceHolder()
            )
            BettingCard(
                title = stringResource(R.string.status),
                icon = bettingTip.status.getStatusResource()
            )
            BettingCard(
                title = stringResource(R.string.odds),
                infoLabel = it.coefficient
            )
        }
    }
}

@Composable
fun BettingCard(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int? = null,
    infoLabel: String? = null
) {
    Surface(
        modifier = modifier
            .height(80.dp)
            .width(80.dp),
        shape = RoundedCornerShape(20.dp),
        color = SilverChalice
    ) {
        Column(
            modifier = modifier.padding(
                start = 5.dp,
                end = 5.dp,
                top = 0.dp,
                bottom = 0.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier.padding(start = 5.dp, top = 2.dp),
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Aldrich,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(10.dp))
            if (icon != null) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon status",
                    modifier = modifier.height(20.dp)
                )
            } else Text(text = infoLabel.toString(),fontFamily = Aldrich,
                textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
fun TeamInfo(modifier: Modifier = Modifier, team: Team? = null) {
    team?.let {
        with(it){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(data = this@with.logo),
                    contentDescription = "Sport image",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = this@with.name,
                    fontFamily = Aldrich,
                    overflow = TextOverflow.Clip,
                    softWrap = true
                )
            }
        }
    } ?: throw NullPointerException("Team must not be null")
}

@Composable
fun CenteredItem(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}
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