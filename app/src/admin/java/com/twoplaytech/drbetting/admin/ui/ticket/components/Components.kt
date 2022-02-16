package com.twoplaytech.drbetting.admin.ui.ticket.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 14:00
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun TicketCard(
    modifier: Modifier = Modifier,
    ticket: Ticket? = null,
    onClick: (String) -> Unit = {}
) {
    ticket?.let {
        Card(
            modifier = modifier
                .padding(bottom = 10.dp)
                .clickable { onClick.invoke(ticket.id) },
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                with(it) {
                    Text(
                        text = "Ticket ${this.id}",
                        modifier = modifier.padding(5.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Date: ${this.date}", modifier = modifier.padding(5.dp),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "Tips available: ${this.tips.size}",
                        modifier = modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TicketsAppBar(
    modifier: Modifier = Modifier,
    title: String,
    activity: ComponentActivity
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { activity.onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow back")
            }
        },
        backgroundColor = colorResource(id = R.color.seagreen)
    )
}
