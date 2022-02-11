package com.twoplaytech.drbetting.sportsanalyst.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Team
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.Aldrich
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SilverChalice

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
                    Text(text = it.leagueName,fontFamily = Aldrich)
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
            BettingCard(title = "Betting tip", infoLabel = it.bettingType)
            BettingCard(title = "Status", icon = R.drawable.tip_won)
            BettingCard(title = "Odds", infoLabel = it.coefficient)
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
            .width(100.dp),
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
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Aldrich
            )
            Spacer(modifier = modifier.height(10.dp))
            if (icon != null) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Icon status",
                    modifier = modifier.height(20.dp)
                )
            } else Text(text = infoLabel.toString())
        }
    }
}

@Preview
@Composable
fun TeamInfo(modifier: Modifier = Modifier, team: Team? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(data = team?.logo),
            contentDescription = "Sport image",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = team!!.name,
            fontFamily = Aldrich,
            overflow = TextOverflow.Clip,
            softWrap = true
        )
    }
}
