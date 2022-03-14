package com.twoplaytech.drbetting.sportsanalyst.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.SeaGreen

/*
    Author: Damjan Miloshevski 
    Created on 14.3.22 08:12
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun SportsAnalystAppBar(
    title: String,
    hasBackNavigation:Boolean = false,
    onBackPressed: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    if (hasBackNavigation) {
        TopAppBar(
            title = {
                Text(
                    text = title
                )
            }, navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back icon",
                    modifier = Modifier.clickable { onBackPressed.invoke() }
                )
            },
            actions = actions, backgroundColor = SeaGreen
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title
                )
            },
            actions = actions, backgroundColor = SeaGreen
        )
    }
}
